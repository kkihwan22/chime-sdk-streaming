const contextUrl = window.location.origin;
const liveId = document.getElementById("hidden-liveId").value;

const getCookie = (name) => {
    const nameLenPlus = name.length + 1;
    const cookie = document.cookie
        .split(";")
        .map((c) => c.trim())
        .filter((cookie) => cookie.substring(0, nameLenPlus) === `${name}=`)
        .map((cookie) => decodeURIComponent(cookie.substring(nameLenPlus)))[0]
    return cookie ?? null
};

const checkValidAuth = () => {
    const cookieName = document.getElementById("hidden-cookie-name").value;
    const cookie = getCookie(cookieName);
    if (!cookie) {
        alert("탈잉 로그인이 필요합니다");
        let replaceUrl = (document.getElementById("hidden-context").value).concat("/Account/LoginPage.php");
        window.location.replace(replaceUrl);
    }
}


function setView(liveId) {
    const liveInfoUrl = contextUrl + '/lives/' + liveId;
    fetch(liveInfoUrl, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            drawLiveInfo(body);
        })
        .catch(err => console.log("err:" + err));

    const attendeeInfoUrl = contextUrl + '/lives/' + liveId + '/attendees';
    fetch(attendeeInfoUrl, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            drawAttendeesInfo(body);
        })
        .catch(err => console.log("err:" + err));

    const ivsInfoUrl = contextUrl + '/internals/lives/' + liveId + '/ivsInfo';
    fetch(ivsInfoUrl, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            drawIvsInfo(body);
        })
        .catch(err => console.log("err:" + err));
}

const drawLiveInfo = (body) => {
    const liveInfoTbl = document.getElementById("tbl-live-info").querySelector('tbody');
    $("#tbl-live-info tbody tr").remove();
    const liveInfoRow = liveInfoTbl.insertRow(-1);
    liveInfoRow.insertCell(0).innerText = body.data.liveId;
    liveInfoRow.insertCell(1).innerText = body.data.title;
    liveInfoRow.insertCell(2).innerText = body.data.liveMethod;
    liveInfoRow.insertCell(3).innerText = body.data["tutor"].userId;
    liveInfoRow.insertCell(4).innerText = body.data["tutor"].nickname;
}

const drawIvsInfo = (body) => {
    const ivsInfoTbl = document.getElementById("tbl-ivs-info").querySelector('tbody');
    $("#tbl-ivs-info tbody tr").remove();
    const ivsInfoRow = ivsInfoTbl.insertRow(-1);
    ivsInfoRow.insertCell(0).innerText = body.data.channelArn;
    ivsInfoRow.insertCell(1).innerText = body.data.channelName;
    ivsInfoRow.insertCell(2).innerText = body.data.ingestEndpoint;
    ivsInfoRow.insertCell(3).innerText = body.data.streamUrl;
    ivsInfoRow.insertCell(4).innerText = body.data.streamKeyArn;
    ivsInfoRow.insertCell(5).innerText = body.data.streamKeyValue;
}

const drawAttendeesInfo = (body) => {
    const attendeesInfoTbl = document.getElementById("tbl-attendees-info").querySelector('tbody');
    $("#tbl-attendees-info tbody tr").remove();
    body.data.forEach(function (item, idx) {
        const row = attendeesInfoTbl.insertRow(-1);
        row.insertCell(0).innerText = idx + 1;
        row.insertCell(1).innerText = item.userId;
        row.insertCell(2).innerText = item.nickname;
        row.insertCell(3).innerText = item.attendeeType;
        row.insertCell(4).innerText = item.videoStatus;
        row.insertCell(5).innerText = item.micStatus;
        row.insertCell(6).innerText = item.screenStatus;
        row.insertCell(7).innerText = item.handsStatus;
    });
}

checkValidAuth();
setView(liveId);