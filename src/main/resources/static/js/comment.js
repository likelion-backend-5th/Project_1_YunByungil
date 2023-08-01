const createButton = document.getElementById('comment-create-btn')

if (createButton) {
    createButton.addEventListener('click', e => {
        let itemId = document.getElementById('item-id').value
        window.alert(itemId)
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