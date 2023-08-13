const createChatButton = document.getElementById('chat-btn')
if (createChatButton) {
    createChatButton.addEventListener('click', async e => {
        const id = document.getElementById("item-id").value;
        // const roomName = document.getElementById("room-name").value;

        try {
            const response = await fetch(`/chat/rooms`, {
                method: "POST",
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    "content-type": "application/json"
                },
                body: JSON.stringify({
                    itemId: id,
                    roomName: 'chat'
                })
            });

            if (response.ok) {
                const responseData = await response.json();
                const roomId = responseData.id;
                alert('gd = '+responseData.roomName)
                console.log(responseData.id);
                window.location.href = `/chat/${roomId}/${id}`;
            } else {
                console.error('Failed to create room');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    });
}

const chatListButton = document.getElementById('chat-list-btn')

if (chatListButton) {
    chatListButton.addEventListener('click', e => {

        fetch('/chat/rooms', {
            method: 'GET',
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                'Content-Type': 'application/json'
            },
        })
            .then(response => {
                response.json().then(responseBody => {
                    const roomDiv = document.getElementById('room-list')
                    roomDiv.innerText = ''
                    responseBody.forEach(chatRoom => {
                        console.log(chatRoom)
                        const newRoom = document.createElement('div');
                        // const roomLink = document.createElement('a');
                        // roomLink.href = `/chat/${chatRoom.id}`;
                        // roomLink.textContent = chatRoom.roomName;
                        // newRoom.appendChild(roomLink);
                        roomDiv.appendChild(newRoom);
                    })
                    alert('성공!')
                    console.log(responseBody)
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
            return success();
        } else {
            return fail();
        }
    })
}