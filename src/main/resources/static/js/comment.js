const createButton = document.getElementById('comment-create-btn')

if (createButton) {
    createButton.addEventListener('click', e => {
        let itemId = document.getElementById('item-id').value
        body = JSON.stringify({
            content: document.getElementById('content').value
        })

        function success() {
            alert('댓글 등록 완료')

        }

        function fail() {
            alert('댓글 등록 실패')
            console.log(fail)
        }

        httpRequest('POST', `/items/${itemId}/comments`, body, success, fail)
    })
}


// 답글 버튼을 클릭했을 때 동작하는 함수
function replyToComment(event) {
    // 클릭한 버튼이 속한 댓글 요소 가져오기
    const commentElement = event.target.closest('.comment')

    // 댓글 요소에 저장된 data-commentid 속성 값 가져오기
    const commentId = commentElement.dataset.commentid

    const commentIdInput = commentElement.querySelector('.comment-id')
    const commentIdValue = commentIdInput.value

    const replyInput = commentElement.querySelector('.reply-input')
    const replyContent = replyInput.value

    // 댓글 ID 값을 콘솔에 출력해보기
    console.log('댓글 ID (data-commentid):', commentId)
    console.log('댓글 ID (input value):', commentIdValue)

    let itemId = document.getElementById('item-id').value
    body = JSON.stringify({
        reply: replyContent
    })

    function success() {
        alert('답글 등록 완료')

    }

    function fail() {
        alert('답글 등록 실패')
        console.log(fail)
    }

    httpRequest('PUT', `/items/${itemId}/comments/${commentIdValue}/reply`, body, success, fail)
}

const replyButtons = document.querySelectorAll('.reply-btn')
replyButtons.forEach(button => {
    button.addEventListener('click', replyToComment)
})

function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json'
        },
        body: body
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        } else {
            return fail();
        }
    })
}