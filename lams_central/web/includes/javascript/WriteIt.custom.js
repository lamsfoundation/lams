/**
 * @license MIT
 * @author Khushit Shah
 */
class WriteIt {
    /**
     * Creates new WriteItJS class handles starting of animation of nodes and SEO.
     */
    constructor() {
        this.WRITEIT_ANIMATE = "writeit-animate";
        this.WRITEIT_AUTO_START = "writeit-autostart";
        this.WRITEIT_CHAR = "writeit-char";
        this.WRITEIT_REPLACE_NEXT = "writeit-replace-next";
        this.WRITEIT_SPEED = "writeit-speed";
        this.WRITEIT_SPEED_FIXED = "writeit-speed-fixed";
        this.WRITEIT_HAS_PREV = "writeit-has-prev";
        this.WRITEIT_START_DELAY = "writeit-start-delay";
        this.WRITEIT_LOOP = "writeit-loop";
        this.WRITEIT_NEXT = "writeit-next";
        this.WRITEIT_HIDDEN = "writeit-hidden";
        this.WRITEIT_NO_BLINKING_WRITEIT = "writeit-no-blink";
        this.WRITEIT_LOOP_REVERSE = "writeit-loop-reverse";
        this.WRITEIT_WAIT_IN_REVERSE = "writeit-wait-in-reverse";
        this.WRITEIT_WRITE_ALL_IN_REVERSE = "writeit-write-all-in-reverse";

        this.count = 0;
        /**
         * @type HTMLElement[]
         */
        this.nodes = [];

        this.init();
        /**  }

         * @type WriteItNode[]
         */
        this.writeitNodes = [];
    }

    /**
     * Starts animating whole page.
     */
    startAnimation() {
        this.nodes.forEach(element => {
            let c = new WriteItNode(element, false, this.count++);
            this.writeitNodes.push(c);
            c.startAnimation();
        });
    }

    /**
     * Pauses the animation of node, if it is currently animating.
     * @param {string} node
     */
    pauseAnimation(node) {
        node = this.findNode(node);
        if (node) node.pauseAnimation();
    }

    /**
     * Return a WriteItNode that maches the selector and already parsed by writeit.
     * @param {string} node
     */
    findNode(node) {
        node = document.querySelector(node);
        let writeItNode = null;
        this.writeitNodes.forEach(_node => {
            if (_node.node == node) {
                writeItNode = _node;
                return false;
            }
            return true;
        });
        return writeItNode;
    }

    /**
     * Resumes the animation of code if it's currently paused.
     * @param {string} node
     */
    resumeAnimation(node) {
        let toResumeNode = this.findNode(node);
        if (toResumeNode) toResumeNode.resumeAnimation();
    }


    /**
     * Starts a animation of a specific node.
     * If the node is already animating stops it and starts new animation.
     * @param node String representing ID of HTML element to Animate.
     */
    startAnimationOfNode(node, fromAnotherNode) {
        let writeitNode = this.findNode(node);
        if (writeitNode) {
            writeitNode.stopAnimation();
            writeitNode.init();
        } else {
            writeitNode = new WriteItNode(node, false, this.count++);
            this.writeitNodes.push(writeitNode);
        }
        if (fromAnotherNode)
            writeitNode.fromAnotherNode = true;
        writeitNode.startAnimation();
    }

    init() {
        this.nodes = Array.from(
            document.querySelectorAll("*[" + this.WRITEIT_ANIMATE + "]")
        );

        this.seo();
    }

    /**
     * Adds a Cloned Hidden Node to html for SEO optimization, as
     * The original node text is going to change.
     */
    seo() {
        this.nodes.forEach(element => {
            const newELement = element.cloneNode(true);
            newELement.id = "seo_" + element.id;
            newELement.style.display = "none";
            element.parentNode.appendChild(newELement);
        });
    }
}

class WriteItNode {
    /**
     * Creates A Node to Animate.
     * @param {HTMLElement} node
     * @param {boolean} _fromAnotherNode
     */
    constructor(node, _fromAnotherNode, _id) {
        /**
         * @type RegExp
         * Looks for a comma after any character.
         */
        this.running = false;
        this.commaSepReg = /,(?!\\)/;
        if (node == undefined || node == null) {
            throw new Error("Node must be a valid HTML tag");
        }
        this.node = node;
        this.fromAnotherNode = _fromAnotherNode;

        this.waitIndex = [];
        this.writeAllTextAtOnceIndex = [];

        this.waitIndex["default"] = [];
        this.writeAllTextAtOnceIndex["default"] = [];

        this.wait = false;
        this.id = _id;
        this.timeout = 0;
        this.interval = 0;
        this.innerHTML = this.node.innerHTML;
        this.texts = [];
        this.textsIndex = -1;
        this.init();
    }

    init() {
        if (this.node.hasAttribute(WriteItJS.WRITEIT_HIDDEN)) {
            this.pdisp = this.node.style.display;
            this.node.style.display = "none";
        }
        if (this.node.hasAttribute(WriteItJS.WRITEIT_CHAR)) {
            this.writeitChar = this.node.getAttribute(WriteItJS.WRITEIT_CHAR);
        } else {
            this.writeitChar = "|";
        }
        if (this.node.hasAttribute(WriteItJS.WRITEIT_REPLACE_NEXT)) {
            this.originalTexts = this.node
                .getAttribute(WriteItJS.WRITEIT_REPLACE_NEXT)
                // remove redudant spaces.
                .replace(/[\s]+/g, " ")
                .split("")
                .reverse()
                .join("")
                .split(this.commaSepReg)
                .map(s =>
                    s
                        .split("")
                        .reverse()
                        .join("")
                        .replace("\\,", ",")
                )
                .reverse();
            // remove redudant spaces
            this.text = this.node.innerHTML.replace(/[\s]+/g, " ");
            this.textsIndex = -1;
        } else {
            // remove redudant spaces
            this.text = this.node.innerHTML.replace(/[\s]+/g, " ");
        }
        this.text = this.text.trim();

        // parse text in order.
        let temp = 0;
        let i;
        while (this.text.indexOf('${', temp) != -1 || this.text.indexOf('$`', temp) != -1) {
            let waitTempIndex = this.text.indexOf('${', temp);
            let writeTempIndex = this.text.indexOf('$`', temp);
            if (waitTempIndex > -1 && (waitTempIndex < writeTempIndex || writeTempIndex == -1)) {
                // set wait.
                // TODO: Check that matching closing bracket is present!
                i = waitTempIndex;
                let secToWait = this.text.substring(i + 2, this.text.indexOf('}', i));
                this.waitIndex["default"][i] = Number(secToWait);
                //                                                   this might return -1
                this.text = this.text.replace(this.text.substring(i, this.text.indexOf("}", i) + 1), "");
                temp += 1;
            } else if (writeTempIndex > -1 && (writeTempIndex < waitTempIndex || waitTempIndex == -1)) {
                // set write.
                i = writeTempIndex;

                // Check that matching quote(`) is present!
                let endIndex = this.text.indexOf("`", i + 2);
                if (endIndex != -1) {
                    let length = endIndex - i - 2;
                    this.writeAllTextAtOnceIndex["default"][i] = endIndex - 1;
                    if (this.node.hasAttribute(WriteItJS.WRITEIT_WRITE_ALL_IN_REVERSE)) {
                        this.writeAllTextAtOnceIndex["default"][i + length] = i;
                    }
                    this.text = this.text.replace(this.text.substring(i, endIndex + 1), this.text.substring(i + 2, endIndex));
                    temp += endIndex;
                }
            }
        }
        if (this.node.hasAttribute(WriteItJS.WRITEIT_REPLACE_NEXT)) {
            // Loop for all texts in WRITEIT_REPLACE_NEXT and also parse them.
            for (let iterator = 0; iterator < this.originalTexts.length; iterator++) {
                let tempText = this.originalTexts[iterator];

                this.waitIndex[iterator] = [];
                this.writeAllTextAtOnceIndex[iterator] = [];

                let temp = 0;
                let i;
                while (tempText.indexOf('${', temp) != -1 || tempText.indexOf('$`', temp) != -1) {
                    let waitTempIndex = tempText.indexOf('${', temp);
                    let writeTempIndex = tempText.indexOf('$`', temp);
                    if (waitTempIndex > -1 && (waitTempIndex < writeTempIndex || writeTempIndex == -1)) {
                        // set wait.
                        i = waitTempIndex;
                        let secToWait = tempText.substring(i + 2, tempText.indexOf('}', i));
                        this.waitIndex[iterator][i] = secToWait;
                        tempText = tempText.replace(tempText.substring(i, tempText.indexOf("}", i) + 1), " ");
                        temp += 2;
                    } else if (writeTempIndex > -1 && (writeTempIndex < waitTempIndex || waitTempIndex == -1)) {
                        // set write.
                        i = writeTempIndex;
                        let endIndex = tempText.indexOf("`", i + 2);
                        this.writeAllTextAtOnceIndex[iterator][i + 1] = endIndex - 1;
                        if (this.node.hasAttribute(WriteItJS.WRITEIT_WRITE_ALL_IN_REVERSE)) {
                            this.writeAllTextAtOnceIndex[iterator][endIndex] = i + 1;
                        }
                        tempText = tempText.replace(tempText.substring(i, endIndex + 1), tempText.substring(i + 2, endIndex));
                        temp += endIndex + 1;
                    }
                }

                this.texts[iterator] = tempText + " "; // add space as wait time can be at last.
            }
        }

        this.speed = 0;
        this.index = 0;
        this.reverse = false;

        this.setSpeed();
    }

    /**
     * Sets a speed for the animation.
     * If writeit-speed-* is present than it uses that,
     * otherwise uses default speed of "6.5±1" letters/second.
     */
    setSpeed() {
        this.speed = 1000 / (Math.random() * (7.5 - 5.5) + 5.5); // Time in millis to write one letter.
        if (this.node.hasAttribute(WriteItJS.WRITEIT_SPEED_FIXED)) {
            this.speed =
                1000 / parseFloat(this.node.getAttribute(WriteItJS.WRITEIT_SPEED_FIXED));
        } else if (this.node.hasAttribute(WriteItJS.WRITEIT_SPEED)) {
            this.speed =
                1000 /
                (Math.random() * 3 +
                    (parseFloat(this.node.getAttribute(WriteItJS.WRITEIT_SPEED)) - 1));
        }
    }

    /**
     * Starts Animation according to factors like delay, before element etc.
     */
    startAnimation() {
        let delay = 0;
        if (
            this.fromAnotherNode ||
            !this.node.hasAttribute(WriteItJS.WRITEIT_HAS_PREV)
        ) {
            this.node.innerHTML = "&nbsp;";
            if (this.node.hasAttribute(WriteItJS.WRITEIT_START_DELAY)) {
                delay =
                    1000 *
                    parseFloat(this.node.getAttribute(WriteItJS.WRITEIT_START_DELAY));
            }
        } else {
            // Element has "writeit-has-prev" attribute So, Don't start animating.
            return;
        }
        // check if the node is hidden?
        if (this.node.hasAttribute(WriteItJS.WRITEIT_HIDDEN)) {
            this.node.style.display = this.pdisp;
        }

        if (this.node.hasAttribute(WriteItJS.WRITEIT_NEXT)) {
            let nodes = this.node.getAttribute(WriteItJS.WRITEIT_NEXT).split(",");
            nodes.forEach(node => {
                let curNode = WriteItJS.findNode(node);
                if (curNode && curNode.running) {
                    curNode.stopAnimation();
                    curNode.init();
                }
            });
        }
        this.blinkCursor(delay, () => {
            this.animate();
            this.running = true;
        })
    }

    stopAnimation() {
        clearTimeout(this.timeout);
        clearInterval(this.interval);
        this.timeout = -1; // Stop executing animation() now;
        this.node.innerHTML = this.innerHTML;
        this.running = false;
    }

    /**
     * Pauses animation.
     */
    pauseAnimation() {
        clearTimeout(this.timeout);
        this.timeout = -1;
    }

    /**
     * Resumes animation.
     */
    resumeAnimation() {
        this.setSpeed();
        this.timeout = this.setTimeout(this.animate, this.speed);
    }

    /**
     * Adds/Removes a letter from "html";
     */
    animate() {
        // return if animation is paused or stopped.
        if (this.timeout == -1) {
            return;
        }
        // Wait if we need to wait at these position.
        if (this.waitIndex[this.textsIndex < 0 ? "default" : this.textsIndex][this.index] != undefined && !this.wait) {
            let secsToWait = this.waitIndex[this.textsIndex < 0 ? "default" : this.textsIndex][this.index];
            this.node.innerHTML = this.text.substring(0, this.index) + "&nbsp;";
            if (this.reverse && this.node.hasAttribute(WriteItJS.WRITEIT_WAIT_IN_REVERSE)) {
                secsToWait = secsToWait * 1000;
                this.index--;
            } else if (!this.reverse) {
                secsToWait = secsToWait * 1000;
                this.index++;
            } else {
                secsToWait = 0;
                this.index--;
            }
            if (this.index <= 0 || this.index >= this.text.length) {
                this.handleIterationEnd();
                return;
            } else {
            }

//            this.timeout = this.setTimeout(() => {
//                this.wait = false;
//                this.animate();
//                }, secsToWait);

            this.blinkCursor(secsToWait, ()=> {
                this.animate();
            })

            return;
        }
        // return if already waiting!
        if (this.wait) return;

        // Write all text at once.
        if (this.writeAllTextAtOnceIndex[this.textsIndex < 0 ? "default" : this.textsIndex][this.index] != undefined) {
            let destinationIndex = this.writeAllTextAtOnceIndex[this.textsIndex < 0 ? "default" : this.textsIndex][this.index];
            if (this.reverse && destinationIndex < this.index) {
                this.node.innerHTML = this.text.substring(0, destinationIndex);
                this.index = destinationIndex - 1;
            } else if (!this.reverse && destinationIndex > this.index) {
                this.node.innerHTML = this.text.substring(0, destinationIndex);
                this.index = destinationIndex + 1;
            }
            if (this.index <= 0 || this.index >= this.text.length) {
                this.handleIterationEnd();
                return;
            } else {
                this.node.innerHTML += this.writeitChar;
            }
            this.nextIteration();
            return;
        }


        // Browser may have added ending tag so ignore it.
        let str = this.text.substring(0, this.index);

        // Add HTML without writeit-char.
        this.node.innerHTML = (str.trim().length != 0) ? str : "&nbsp;";

        // If reverse remove a character from last.
        if (this.reverse) {
            // We are at start of  the string.
            if (str.length <= 0) {
                this.handleIterationEnd();
                return;
            }
            let chrToRemove = str.substr(str.length - 1, 1);
            if (chrToRemove == ">") {
                // Go back until next < is found.
                chrToRemove = this.text.substring(
                    str.length,
                    str.lastIndexOf("<", str.length)
                );
                str = str.substr(0, str.length - chrToRemove.length);
                this.index -= chrToRemove.length;
            } else if (chrToRemove == ";") {
                // Go back until next & is found.
                chrToRemove = this.text.substring(
                    str.length,
                    str.lastIndexOf("&", str.length)
                );
                str = str.substr(0, str.length - chrToRemove.length);
                this.index -= chrToRemove.length;
            } else {
                str = str.substring(0, str.length - 1);
                this.index--;
            }
        } else {
            if (str.length >= this.text.length) {
                // We are at End in the string.
                this.handleIterationEnd();
                return;
            }
            // Add a letter to the last.
            let chrToAdd = this.text.substr(str.length, 1);

            if (chrToAdd == "<") {
                // Take everything until ">".
                chrToAdd = this.text.substring(
                    str.length,
                    this.text.indexOf(">", str.length) + 1
                );
            } else if (chrToAdd == "&") {
                // handle &...;
                chrToAdd = this.text.substring(
                    str.length,
                    this.text.indexOf(";", str.length) + 1
                );
            }

            this.index += chrToAdd.length;

            str = str + chrToAdd;
        }

        // Add writeit at the end.
        str += this.writeitChar;

        // Update the InnerHTML
        this.node.innerHTML = str;

        this.nextIteration();
    }

    /**
     * Gets Executed after animation is over,
     * It decides whether to rerun or reverse or call animation of another element.
     */
    handleIterationEnd() {
        if (this.reverse) {
            // Check if it has WRITEIT_REPLACE_NEXT, then replace text with next text.
            if (this.node.hasAttribute(WriteItJS.WRITEIT_REPLACE_NEXT)) {
                // If we are already at the last position.
                if (this.textsIndex + 1 >= this.texts.length) {
                    // Start from first if it has writeit-loop-reverse.
                    if (this.node.hasAttribute(WriteItJS.WRITEIT_LOOP_REVERSE)) {
                        // Start animation from the start!
                        this.textsIndex = 0;
                        this.text = this.texts[this.textsIndex];
                        this.trigerNextAnimation(false, 0, true);
                    }

                } else {
                    // Just increment the textsIndex.
                    this.text = this.texts[++this.textsIndex];
                    this.trigerNextAnimation(false, 0, true);
                }
            } else if (this.node.hasAttribute(WriteItJS.WRITEIT_LOOP_REVERSE)) {
                this.trigerNextAnimation(false, 0, true);
            }
        } else {

            if (this.node.hasAttribute(WriteItJS.WRITEIT_REPLACE_NEXT)) {
                // Current Text has been writtern, Start reverse.
                // if this is last text and node don't conatains writeit-loop then stop.
                if (
                    this.textsIndex >= this.texts.length - 1 &&
                    !(this.node.hasAttribute(WriteItJS.WRITEIT_LOOP) || this.node.hasAttribute(WriteItJS.WRITEIT_LOOP_REVERSE))
                ) {
                    this.animationEnd();
                    return;
                } else if (this.textsIndex >= this.texts.length - 1 && this.node.hasAttribute(WriteItJS.WRITEIT_LOOP)) {
                    // Start animation from the start!
                    this.textsIndex = 0;
                    this.text = this.texts[this.textsIndex];
                    this.trigerNextAnimation(false, 0, true);
                } else {
                    this.trigerNextAnimation(true);
                }
            } else if (this.node.hasAttribute(WriteItJS.WRITEIT_LOOP)) {
                this.trigerNextAnimation(false, 0, true);
            } else if (this.node.hasAttribute(WriteItJS.WRITEIT_LOOP_REVERSE)) {
                this.node.innerHTML += this.writeitChar;
                this.trigerNextAnimation(true);
            } else {
                // Add Simple Blinking writeit animation.
                this.animationEnd();
            }
        }
    }

    /**
     * Handles end of the animation of element, starts last blinking writeit or trigers animation of another elements.
     */
    animationEnd() {
        if (this.node.hasAttribute(WriteItJS.WRITEIT_NEXT)) {
            // Go through all of them and start them.
            let nodes = this.node.getAttribute(WriteItJS.WRITEIT_NEXT).split(",");
            nodes.forEach(node => {
                WriteItJS.startAnimation(node, true);
            });
        } else {
            if (!this.node.hasAttribute(WriteItJS.WRITEIT_NO_BLINKING_WRITEIT))
                this.lastWriteItBlinkAnimation();
        }

        const animationFinishedEvent = new CustomEvent("writeitAnimateEnd", { detail: {'writeitElementId' : this.id }});
        document.querySelector('body').dispatchEvent(animationFinishedEvent);

    }

    /**
     * @param {boolean} reverse wether animation is reverse or forward.
     * @param {integer} _index (optional) decides wether to set index.
     * @param {boolean} _reInitNode (optional) decides wether to set node.innerHTML with writeitChar.
     */
    trigerNextAnimation(reverse, _index, _reInitNode) {
        this.reverse = reverse;
        this.index = _index != undefined ? _index : this.index;
        this.node.innerHTML = _reInitNode ? this.writeitChar : this.node.innerHTML;
        this.timeout = this.setTimeout(this.animate, this.speed);
    }

    /**
     * Keeps blinking writeit for infinity.
     */
    lastWriteItBlinkAnimation() {
        this.interval = this.setInterval(() => {
            if (this.node.innerHTML.replace(/'/g, "\"") != (this.text + this.writeitChar).replace(/'/g, "\"")) {
                this.node.innerHTML = this.text + this.writeitChar;
            } else {
                this.node.innerHTML = this.text + ("\u00A0".repeat(this.writeitChar.length));
            }
        }, 500);
    }

    blinkCursor(time, callback) {
        this.curText = this.node.innerHTML;
        this.blinkInterval = this.setInterval(() => {
            if (this.node.innerHTML.replace(/'/g, "\"") != (this.curText + this.writeitChar).replace(/'/g, "\"")) {
                this.node.innerHTML = this.curText + this.writeitChar;
            } else {
                this.node.innerHTML = this.curText + ("\u00A0".repeat(this.writeitChar.length));
            }
        }, 500);

        this.setTimeout(() => {
            clearInterval(this.blinkInterval);
            callback();
        }, time);
    }
    /**
     * Resets the speed and calls the animate function as per delay
     */
    nextIteration() {
        this.setSpeed();
        this.timeout = this.setTimeout(this.animate, this.speed);
    }

    setInterval(method, delay) {
        return setInterval(method.bind(this), delay);
    }

    setTimeout(method, delay) {
        return setTimeout(method.bind(this), delay);
    }
}

const WriteItJS = new WriteIt();

// Automatically start Animation if writeit-auto-start attribute is present,
// this is not related with the animation of node
// but, it starts parsing each node which has "writeit-animate"
if (document.querySelector("*[" + WriteItJS.WRITEIT_AUTO_START + "]")) {
    WriteItJS.startAnimation();
}

// add other WriteItJS function as an array object so they don't get ignored by Closure ADVANCE settings.
window["WriteItJS"] = WriteItJS;
window["WriteItJS"]["startAnimation"] = WriteItJS.startAnimation;
window["WriteItJS"]["pauseAnimation"] = WriteItJS.pauseAnimation;
window["WriteItJS"]["resumeAnimation"] = WriteItJS.resumeAnimation;
window["WriteItJS"]["startAnimationOfNode"] = WriteItJS.startAnimationOfNode;
// other functions are not necessary for users[programmers].
console.log("WriteIt.js v2.0 loaded!, visit https://github.com/khushit-shah/WriteIt.js"); 