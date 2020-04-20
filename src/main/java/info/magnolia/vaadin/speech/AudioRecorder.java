package info.magnolia.vaadin.speech;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.function.SerializableConsumer;

import elemental.json.JsonArray;

@JavaScript("recorder.js")
@JavaScript("jquery-3.2.0.js")
@JavaScript("audiorecorder_connector.js")
@Tag("div")
public class AudioRecorder extends Component {

    public AudioRecorder() {
    }

    @ClientCallable
    public void stopServerRecording(JsonArray arguments) {
        final byte[] wavBinary = toByteArray(arguments.getArray(0));
        for (ValueChangeListener listener : listeners) {
            listener.valueChange(wavBinary);
        }
    }

    private byte[] toByteArray(final JsonArray wavBytes) {
        final byte[] bytes = new byte[wavBytes.length()];
        for (int i = 0; i < wavBytes.length(); i++) {
            bytes[i] = (byte) wavBytes.get(i).asNumber();
        }
        return bytes;
    }

    public interface ValueChangeListener extends Serializable {
        void valueChange(byte[] wavBinary);
    }

    private final List<ValueChangeListener> listeners = Lists.newArrayList();

    public void addValueChangeListener(ValueChangeListener listener) {
        listeners.add(listener);
    }

    public void record() {
        runBeforeClientResponse(ui -> {
            ui.getPage().executeJs("startRecording");
        });
    }

    public void stop() {
        runBeforeClientResponse(ui -> {
            ui.getPage().executeJs("stopRecording");
        });
    }

    void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
