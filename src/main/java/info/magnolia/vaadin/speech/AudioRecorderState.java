package info.magnolia.vaadin.speech;

import com.vaadin.shared.ui.JavaScriptComponentState;


public class AudioRecorderState extends JavaScriptComponentState {

    private String fileName;

    public void setFileName(String value) {
        this.fileName = value;
    }

    public String getFileName() {
        return fileName;
    }
}
