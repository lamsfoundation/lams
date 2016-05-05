package org.eucm.lams.tool.eadventure.util;

import java.util.HashMap;

public class IOdatamodel {

    private HashMap<String, String> input;

    private HashMap<String, String> output;

    public IOdatamodel() {
	input = new HashMap<String, String>();

	output = new HashMap<String, String>();
    }

    public void addInput(String name, String type) {
	input.put(name, type);
    }

    public void addOutput(String name, String type) {
	output.put(name, type);
    }

    public HashMap<String, String> getInput() {
	return input;
    }

    public void setInput(HashMap<String, String> input) {
	this.input = input;
    }

    public HashMap<String, String> getOutput() {
	return output;
    }

    public void setOutput(HashMap<String, String> output) {
	this.output = output;
    }

}
