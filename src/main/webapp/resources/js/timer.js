function startTimer() {
   let timer = 3;
   setInterval(() => {
    // console.log(--timer);
    if (timer >= 0) {
        let min = Math.floor(timer / 60);
        let sec = String(timer % 60).padStart(2, '0');
        let remainTime = min + ":" + sec;
        $('.timer').html(remainTime);
        --timer;
    } else {
    	$('#authBtn').prop('disabled', true);
    	// 백엔드에 인증시간이 만료되었음을 알려야함
    }
    
   }, 1000);
}