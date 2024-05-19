const axios = require('axios');
const crypto = require('crypto');
const { format } = require('date-fns');

const apiUrls = ["http://localhost:8881/api/kafka/publish", "http://localhost:8882/api/kafka/publish"];

function getRandomString(length) {
    return crypto.randomBytes(length).toString('hex').slice(0, length);
}

async function sendRequest(seq) {
    const url = apiUrls[Math.floor(Math.random() * apiUrls.length)];

    const username = getRandomString(5);
    const password = getRandomString(5);
    const formattedDate = format(new Date(), 'yyyy-MM-dd HH:mm:ss');
    const createBy = url.includes("8881") ? "producer_8881" : "producer_8882";

    const jsonData = {
        seq: seq,
        username: username,
        password: password,
        createBy: createBy,
        produceTime: new Date().toISOString()
    };

    try {
        console.log(`Request ${seq} to ${url} with data: ${JSON.stringify(jsonData)}`);
        const response = await axios.post(url, {'data':JSON.stringify(jsonData)}, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        console.log(`Request ${seq} to ${url} succeeded: ${response.data}`);
    } catch (error) {
        console.log(`Request ${seq} to ${url} failed: ${error.message}`);
    }
}

const maxConcurrentRequests = 20;
let activeRequests = 0;
const requestQueue = [];

function processQueue() {
    while (activeRequests < maxConcurrentRequests && requestQueue.length > 0) {
        const { seq, resolve } = requestQueue.shift();
        activeRequests++;
        sendRequest(seq).then(() => {
            activeRequests--;
            resolve();
            processQueue();
        });
    }
}

async function main() {
    console.log("*************************");
    const requests = [];
    const count = 10000

    for (let i = 1; i <= count; i++) {
        requests.push(new Promise(resolve => {
            requestQueue.push({ seq: i, resolve });
            processQueue();
        }));
    }

    await Promise.all(requests);
    console.log("All jobs completed.");
}

main().catch(error => console.error(error));
