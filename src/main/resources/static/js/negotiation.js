const negotiationCreateButton = document.getElementById('negotiation-create-btn')

if (negotiationCreateButton) {
    negotiationCreateButton.addEventListener('click', e => {
        let itemId = document.getElementById('item-id').value
        body = JSON.stringify({
            suggestedPrice: document.getElementById('suggestedPrice').value
        })

        function success() {
            alert('가격 제안 완료')

        }

        function fail() {
            alert('가격 제안 실패')
            console.log(fail)
        }

        httpRequest('POST', `/items/${itemId}/proposals`, body, success, fail)
    })
}

// 조회 api
const negotiationButton = document.getElementById('negotiation-btn')

if (negotiationButton) {
    negotiationButton.addEventListener('click', e => {
        let itemId = document.getElementById('item-id').value
        window.alert(itemId)
        const url = `/items/${itemId}/proposals`

        fetch(url, {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                'Content-Type': 'application/json'
            },
        }).then(response => {
            console.log('hi')
            console.log(response.status)
            if (response.ok) response.json().then(body => {
                console.log(body.content)
            })
        })
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
            // console.log(response.body)
            return success();
        } else {
            return fail();
        }
    })
}