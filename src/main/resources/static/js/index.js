const contextUrl = window.location.origin;

const findClassroom = () => {
    const url = contextUrl.concat("/demo/meetings?size=10")
    fetch(url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            drawClassrooms(body);
        })
        .catch(err => console.log("err:" + err));
}

const createClassroom = (className, onRec) => {
    const url = contextUrl.concat("/demo/classrooms")
    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            'className': className,
            'onRec': onRec
        })
    })
        .then(response => {
            console.log(response)
            findClassroom();
        })
        .catch(err => console.log(err))
}

const drawClassrooms = (body) => {
    const classroom = document.getElementById("classrooms").querySelector('tbody');
    $("#classrooms tbody tr").remove();
    body.data.meetings.forEach(function (item, idx) {
        const meeting = item.meeting;
        const meetingId = meeting.meetingId;

        const row = classroom.insertRow(-1);
        const cell_0 = row.insertCell(0);
        cell_0.innerText = idx + 1;

        const cell_1 = row.insertCell(1);
        cell_1.innerText = meeting.externalMeetingId;

        const cell_2 = row.insertCell(2);
        const buttonElement = document.createElement('button');
        buttonElement.innerText = "입장하기";
        buttonElement.dataset.meetingId = meetingId;
        buttonElement.addEventListener("click", enterToMeeting);
        cell_2.appendChild(buttonElement);

        const cell_3 = row.insertCell(3);
        cell_3.innerText = item.countOfAttendee;
    });
}

const enterToMeeting = (event) => {
    const url = "https://live-stag-front.taling.me";
    const dataMeetingId = event.target.dataset.meetingId;
    window.location.href = `${url}/${dataMeetingId}`;
}

// const getCookie = (name) => {
//     const nameLenPlus = name.length + 1;
//     const cookie = document.cookie
//             .split(";")
//             .map((c) => c.trim())
//             .filter((cookie) => cookie.substring(0, nameLenPlus) === `${name}=`)
//             .map((cookie) => decodeURIComponent(cookie.substring(nameLenPlus)))[0]
//     return cookie ?? null
// };
//
// const checkValidAuth = () => {
//     const cookie = getCookie("PHPSESSID");
//     if (!cookie) {
//         alert("탈잉 로그인이 필요합니다");
//         window.location.replace("https://dev.taling.me/Account/LoginPage.php");
//     }
// }

document.getElementById('btn-create-classroom').onclick = function () {
    createClassroom(document.getElementById("className").value, document.getElementById("on-rec").checked);
}

// checkValidAuth();
findClassroom();
