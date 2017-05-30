console.log('Connector fckn LOADED!');

window.info_magnolia_vaadin_speech_AudioRecorder = function () {
    var element = $(this.getElement());
    var self = this;

    var audio_context;
    var recorder;

        // browser prefix shims
        window.AudioContext = window.AudioContext || window.webkitAudioContext;
        navigator.getUserMedia = navigator.getUserMedia || navigator.mozGetUserMedia || navigator.webkitGetUserMedia;
        window.URL = window.URL || window.webkitURL;

        audio_context = new AudioContext;
        console.log('Audio context set up.');
        console.log('navigator.getUserMedia ' + (navigator.getUserMedia ? 'available.' : 'not present!'));

    try {
        navigator.getUserMedia({audio: true}, startUserMedia, function (e) {
            console.log('No live audio input: ' + e);
        });
    } catch (e) {
        console.error('No web audio support in this browser!');
        throw e;
    }


    function startUserMedia(stream) {
        var input = audio_context.createMediaStreamSource(stream);
        console.log('Media stream created.');
        // Uncomment if you want the audio to feedback directly
        //input.connect(audio_context.destination);
        //__log('Input connected to audio context destination.');

        recorder = new Recorder(input);
        console.log('Recorder initialised.');
    }

    this.startRecording = function () {
        recorder && recorder.record();
        console.log('Recording...');
    };

    this.stopRecording = function () {
        if (!recorder) return;

        recorder.stop();
        console.log('Stopped recording.');

        recorder.exportWAV(function (blob) {
            var reader = new FileReader();

            reader.addEventListener('loadend', function() {
                // var wavBytes = new Int8Array(reader.result);
                self.stopServerRecording(reader.result);
                recorder.clear();
            });

            reader.readAsText(blob, "UTF-8");
        });
    };
};
