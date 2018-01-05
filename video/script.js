window.onload = function () {
	var video = document.getElementById("video");

	var playButton = document.getElementById("play-pause");
	var play1speed = document.getElementById("play1x");
	var play1_5speed = document.getElementById("play1.5x");
	var play2speed = document.getElementById("play2x");

	var fullScreenButton = document.getElementById("full-screen");

	var seekBar = document.getElementById("seek-bar");

	playButton.addEventListener("click", function () {
		if (video.paused) {
			video.play();

			playButton.innerHTML = "Pause";
		} else {
			video.pause();
			playButton.innerHTML = "Play";
		}
	});
	play1speed.addEventListener("click", function () {
		video.playbackRate = 1.0;
		if (video.paused) {
			video.play();
		}
	});
	play1_5speed.addEventListener("click", function () {
		video.playbackRate = 1.0;
		if (video.paused) {
			video.play();
		}
	});
	play2speed.addEventListener("click", function () {
		video.playbackRate = 2.0;
		if (video.paused) {
			video.play();
		}
	});
	seekBar.addEventListener("change", function () {
		var time = video.duration * (seekBar.value / 100);
		video.currentTime = time;
	});

	video.addEventListener("timeupdate", function () {
		var value = (100 / video.duration) * video.currentTime;

		seekBar.value = value;
	});

	fullScreenButton.addEventListener("click", function () {
		if (video.requestFullscreen) {
			video.requestFullscreen();
		} else if (video.mozRequestFullScreen) {
			video.mozRequestFullScreen();
		} else if (video.webkitRequestFullscreen) {
			video.webkitRequestFullscreen();
		}
	});
}
