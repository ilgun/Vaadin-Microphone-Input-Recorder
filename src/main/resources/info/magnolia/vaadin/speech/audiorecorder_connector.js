window.info_magnolia_vaadin_speech_AudioRecorder = function () {
    var element = $(this.getElement());
    var self = this;

    var audio_context;
    var recorder;

    try {
        // webkit shim
        window.AudioContext = window.AudioContext || window.webkitAudioContext;
        navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
        window.URL = window.URL || window.webkitURL;

        audio_context = new AudioContext;
        console.log('Audio context set up.');
        console.log('navigator.getUserMedia ' + (navigator.getUserMedia ? 'available.' : 'not present!'));
    } catch (e) {
        alert('No web audio support in this browser!');
    }

    navigator.getUserMedia({audio: true}, startUserMedia, function (e) {
        console.log('No live audio input: ' + e);
    });


    function startUserMedia(stream) {
        var input = audio_context.createMediaStreamSource(stream);
        console.log('Media stream created.');
        // Uncomment if you want the audio to feedback directly
        //input.connect(audio_context.destination);
        //__log('Input connected to audio context destination.');

        recorder = new Recorder(input);
        console.log('Recorder initialised.');
    }

    function createDownloadLink(callback) {
        recorder && recorder.exportWAV(function (blob) {
            var url = URL.createObjectURL(blob);
            callback(url);
        });
    }

    this.startRecording = function () {
        recorder && recorder.record();
        console.log('Recording...');
    };

    this.stopRecording = function () {
        recorder && recorder.stop();
        console.log('Stopped recording.');
        createDownloadLink(function (url) {
            var link = window.document.createElement('a');
            link.href = url;
            link.download = new Date().getUTCMilliseconds() + '.wav';
            link.click();
            self.stopServerRecording(link.download);
            recorder.clear();
        });
    };
};