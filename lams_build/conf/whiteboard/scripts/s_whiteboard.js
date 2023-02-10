//This file is only for saving the whiteboard.
const fs = require("fs");
const config = require("./config/config");
const { getSafeFilePath } = require("./utils");
const FILE_DATABASE_FOLDER = "savedBoards";

var savedBoards = {};
var savedUndos = {};
var saveDelay = {};

if (config.backend.enableFileDatabase) {
    // make sure that folder with saved boards exists
    fs.mkdirSync(FILE_DATABASE_FOLDER, {
        // this option also mutes an error if path exists
        recursive: true
    });
}

/**
 * Get the file path for a whiteboard.
 * @param {string} wid Whiteboard id to get the path for
 * @returns {string} File path to the whiteboard
 * @throws {Error} if wid contains potentially unsafe directory characters
 */
function fileDatabasePath(wid) {
    return getSafeFilePath(FILE_DATABASE_FOLDER, wid + ".json");
}

module.exports = {
    handleEventsAndData: function (content) {
        var tool = content["t"]; //Tool witch is used
        var wid = content["wid"]; //whiteboard ID
        var username = content["username"];
        if (tool === "clear") {
            //Clear the whiteboard
            delete savedBoards[wid];
            delete savedUndos[wid];
            // delete the corresponding file too
            fs.unlink(fileDatabasePath(wid), function (err) {
                if (err) {
                    return console.log(err);
                }
            });
        } else if (tool === "undo") {
            //Undo an action
            if (!savedUndos[wid]) {
                savedUndos[wid] = [];
            }
            let savedBoard = this.loadStoredData(wid);
            if (savedBoard) {
                for (var i = savedBoards[wid].length - 1; i >= 0; i--) {
                    if (savedBoards[wid][i]["username"] == username) {
                        var drawId = savedBoards[wid][i]["drawId"];
                        for (var i = savedBoards[wid].length - 1; i >= 0; i--) {
                            if (
                                savedBoards[wid][i]["drawId"] == drawId &&
                                savedBoards[wid][i]["username"] == username
                            ) {
                                savedUndos[wid].push(savedBoards[wid][i]);
                                savedBoards[wid].splice(i, 1);
                            }
                        }
                        break;
                    }
                }
                if (savedUndos[wid].length > 1000) {
                    savedUndos[wid].splice(0, savedUndos[wid].length - 1000);
                }
            }
        } else if (tool === "redo") {
            if (!savedUndos[wid]) {
                savedUndos[wid] = [];
            }
            let savedBoard = this.loadStoredData(wid);
            for (var i = savedUndos[wid].length - 1; i >= 0; i--) {
                if (savedUndos[wid][i]["username"] == username) {
                    var drawId = savedUndos[wid][i]["drawId"];
                    for (var i = savedUndos[wid].length - 1; i >= 0; i--) {
                        if (
                            savedUndos[wid][i]["drawId"] == drawId &&
                            savedUndos[wid][i]["username"] == username
                        ) {
                            savedBoard.push(savedUndos[wid][i]);
                            savedUndos[wid].splice(i, 1);
                        }
                    }
                    break;
                }
            }
        } else if (
            [
                "line",
                "pen",
                "rect",
                "circle",
                "eraser",
                "addImgBG",
                "recSelect",
                "eraseRec",
                "addTextBox",
                "setTextboxText",
                "removeTextbox",
                "setTextboxPosition",
                "setTextboxFontSize",
                "setTextboxFontColor",
            ].includes(tool)
        ) {
            let savedBoard = this.loadStoredData(wid);
            //Save all this actions
            delete content["wid"]; //Delete id from content so we don't store it twice
            if (tool === "setTextboxText") {
                for (var i = savedBoard.length - 1; i >= 0; i--) {
                    //Remove old textbox tex -> dont store it twice
                    if (
                        savedBoard[i]["t"] === "setTextboxText" &&
                        savedBoard[i]["d"][0] === content["d"][0]
                    ) {
                        savedBoard.splice(i, 1);
                    }
                }
            }
            savedBoard.push(content);
        }
        this.saveToDB(wid);
    },
    saveToDB: function (wid) {
        if (config.backend.enableFileDatabase) {
            //Save whiteboard to file
            if (!saveDelay[wid]) {
                saveDelay[wid] = true;
                setTimeout(function () {
                    saveDelay[wid] = false;
                    if (savedBoards[wid]) {
                        fs.writeFile(
                            fileDatabasePath(wid),
                            JSON.stringify(savedBoards[wid]),
                            (err) => {
                                if (err) {
                                    return console.log(err);
                                }
                            }
                        );
                    }
                }, 1000 * 10); //Save after 10 sec
            }
        }
    },
    // Load saved whiteboard
    loadStoredData: function (wid) {
        if (wid in savedBoards) {
            return savedBoards[wid];
        }

        savedBoards[wid] = [];

        // try to load from DB
        if (config.backend.enableFileDatabase) {
            //read saved board from file
            var filePath = fileDatabasePath(wid);
            if (fs.existsSync(filePath)) {
                var data = fs.readFileSync(filePath);
                if (data) {
                    savedBoards[wid] = JSON.parse(data);
                }
            }
        }

        return savedBoards[wid];
    },
    copyStoredData: function (sourceWid, targetWid) {
        const sourceData = this.loadStoredData(sourceWid);
        if (sourceData.length === 0 || this.loadStoredData(targetWid).lenght > 0) {
            return;
        }
        savedBoards[targetWid] = sourceData.slice();
        
        // LAMS: after load prefix author so his steps can not be undone
		for (var i = 0; i < savedBoards[targetWid].length; i++) {
			let username = savedBoards[targetWid][i]["username"];
			if (username && !username.startsWith('authored-')) {
				savedBoards[targetWid][i]["username"] = 'authored-' + username;
			}
		}     
		
        this.saveToDB(targetWid);
    },
    saveData: function (wid, data, processEmbeddedImages) {
        const existingData = this.loadStoredData(wid);
        if (existingData.length > 0 || !data) {
            return;
        }
        let savedBoard = JSON.parse(data);
		
		// importing LAMS content which has base64 images embedded
		if (processEmbeddedImages) {
			savedBoard.forEach(function(entry) {
				// check if it is an embedded image or one with absolute URL
				if (entry.t !== "addImgBG" || !entry.url.startsWith("/uploads/") || !entry.imageData) {
					return true;
				}
				
				// if the file already exists on the servrer, do nothing
				const filePath = "./public" + entry.url;
				if (fs.existsSync(filePath)) {
					return true;
				}
				
				// create a dir for the given output path
				fs.mkdirSync(filePath.substring(0, filePath.lastIndexOf("/")), {
					// this option also mutes an error if path exists
					recursive : true
				});

				// write out data to file
				fs.writeFileSync(filePath, entry.imageData, "base64");
				
				// clean up
				delete entry.imageData;		
			});
		}
		savedBoards[wid] = savedBoard;
        this.saveToDB(wid);
    }
};
