const contextUrl = window.location.origin;

const finds = () => {
    const url = contextUrl.concat("/lives")
    fetch(url, {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            drawList(body);
        })
        .catch(err => console.log("err:" + err));
}

const drawList = (body) => {
    const tbl = document.getElementById("tbl-lives").querySelector('tbody');
    $("#tbl-lives tbody tr").remove();
    body.data.forEach(function (item, idx) {
        const row = tbl.insertRow(-1);
        row.insertCell(0).innerText = idx + 1;
        row.insertCell(1).innerText = item.title;
        const btnCell = row.insertCell(2);
        const btn1 = document.createElement('button');
        const btn2 = document.createElement('button');
        btn1.innerText = "입장하기";
        btn1.dataset.liveId = item.liveId;
        btn1.addEventListener("click", enter);

        btn2.innerText = "종료하기";
        btn2.dataset.liveId = item.liveId;
        btn2.addEventListener("click", close);

        btnCell.appendChild(btn1);
        btnCell.appendChild(btn2);

        // 라이브 방 정보 확인하기 버튼
        const liveInfoBtnCell = row.insertCell(3);
        const liveInfoBtn = document.createElement('button');
        liveInfoBtn.innerText = "방 정보 확인하기";
        liveInfoBtn.dataset.liveId = item.liveId;
        liveInfoBtn.addEventListener("click", liveInfoEnter);
        liveInfoBtnCell.appendChild(liveInfoBtn);
    });
}

const enter = (event) => {
    //const url = "https://live-stag-front.taling.me";
    const url = document.getElementById("hidden-front").value;
    console.log(url);
    const dataLiveId = event.target.dataset.liveId;
    window.location.href = `${url}/${dataLiveId}`;
}

const liveInfoEnter = (event) => {
    const url = contextUrl.concat("/liveInfo");
    console.log(url);
    const dataLiveId = event.target.dataset.liveId;
    window.location.href = `${url}/${dataLiveId}`;
}

const close = (event) => {
    const url = contextUrl.concat("/internals/lives/").concat(event.target.dataset.liveId).concat("/close");
    fetch(url, {
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json',
        },
        body: {}
    })
        .then(response => response.json())
        .then(body => {
            alert("meeting close success. ");
            finds();
        })
        .catch(err => console.log(err))
}

async function create() {
    const url = contextUrl.concat("/lives")
    const liveType = document.querySelector('input[name="rb-type"]:checked').value;
    const liveMethod = document.querySelector('input[name="rb-live-method"]:checked').value;
    const startDatetime = document.getElementById("start-dt").value;
    const title = document.getElementById("title").value;
    const rec = document.getElementById("cb-rec").checked;
    let liveId;
    let spaceId;

    if (title === null || title === '') {
        alert("미팅방 제목은 반드시 입력해주세요.");
        return;
    }

    if (liveType === 'INSTANCE') {
        try {
            liveId = await getLiveId();
            console.log('liveId : ' + liveId);
            spaceId = await getSpaceId(liveId, title, startDatetime);
            console.log('spaceId : ' + spaceId);
        } catch (err) {
            console.log(err);
        }
    }

    fetch(url, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "liveId": liveId,
            "channelId": spaceId,
            "liveType": liveType,
            "title": title,
            "liveMethod": liveMethod,
            "recCondition": rec,
            "startDatetime": startDatetime
        })
    })
        .then(response => response.json())
        .then(body => {
            console.log(body)
            if (liveType === 'MEETING') {
                alert("[입장 URL] >>>>> " + document.getElementById("hidden-front").value + "/" + body.data.liveId);
            }
            finds();
        })
        .catch(err => console.log(err))
}

function getLiveId() {
    return fetch(contextUrl.concat("/internals/commons/sequence"), {
        method: "GET",
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            return body.data[0];
        })
        .catch(err => console.log(err))
}

function getSpaceId(liveId, title, startDatetime) {
    return fetch(contextUrl.concat("/internals/space"), {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "referenceId": liveId,
            "referenceType": "MEETING",
            "title": title,
            "startedAt": startDatetime
        })
    })
        .then(response => response.json())
        .then(body => {
            console.log(body);
            return body.data.spaceId;
        })
        .catch(err => console.log(err))
}

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


document.querySelectorAll('input[name="rb-type"]').forEach(
    item => item.addEventListener('change', () => {
        console.log(item.value);
        const target = document.getElementById("start-dt");
        if (item.value === 'MEETING') {
            let now = new Date();
            let future = new Date(now.getTime() + 30 * 60000);
            let min = new Date(future - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, -8);
            target.value = min;
            target.setAttribute("min", min);
            target.disabled = false;
        } else {
            target.disabled = true;
        }
    })
);

document.getElementById('btn-create').onclick = function () {
    create();
}

checkValidAuth();
finds();
