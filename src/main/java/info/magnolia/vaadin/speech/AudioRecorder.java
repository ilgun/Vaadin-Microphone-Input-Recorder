package info.magnolia.vaadin.speech;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

@JavaScript({"recorder.js", "audiorecorder_connector.js", "jquery-3.2.0.js"})
public class AudioRecorder extends AbstractJavaScriptComponent {

    public AudioRecorder() {
        this.addFunction("stopServerRecording", (JavaScriptFunction) arguments -> {
            getState().setFileName(arguments.getString(0));
            for (ValueChangeListener listener : listeners) {
                listener.valueChange(getState().getFileName());
            }
        });
    }

    public interface ValueChangeListener extends Serializable {
        void valueChange(String changedValue);
    }

    private List<ValueChangeListener> listeners = Lists.newArrayList();

    public void addValueChangeListener(ValueChangeListener listener) {
        listeners.add(listener);
    }

    public void record() {
        this.callFunction("startRecording");
    }

    public void stop() {
        this.callFunction("stopRecording");
    }

    @Override
    protected AudioRecorderState getState() {
        return (AudioRecorderState) super.getState();
    }
}
