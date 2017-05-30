package info.magnolia.vaadin.speech;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;
import elemental.json.JsonString;

@JavaScript({"recorder.js", "audiorecorder_connector.js", "jquery-3.2.0.js"})
public class AudioRecorder extends AbstractJavaScriptComponent {

    public AudioRecorder() {
        this.addFunction("stopServerRecording", (JavaScriptFunction) arguments -> {
            final byte[] wavBinary = arguments.getString(0).getBytes();
            for (ValueChangeListener listener : listeners) {
                listener.valueChange(wavBinary);
            }
        });
    }

    private byte[] toByteArray(final JsonArray wavBytes) {
        return wavBytes.toString().getBytes();
    }

    public interface ValueChangeListener extends Serializable {
        void valueChange(byte[] wavBinary);
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
