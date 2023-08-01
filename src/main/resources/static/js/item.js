const createButton = document.getElementById('item-create-btn')

if (createButton) {
    createButton.addEventListener('click', e => {
        body = JSON.stringify({
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            minPriceWanted: document.getElementById('minPriceWanted').value
        })

        function success() {
            alert('상품 등록 완료')
            location.replace('/views/items')
        }

        function fail() {
            alert('상품 등록 실패')
            console.log(fail)
        }

        httpRequest('POST', '/items', body, success, fail)
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