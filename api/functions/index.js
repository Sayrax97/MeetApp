const functions = require("firebase-functions");
const admin = require("firebase-admin");
const express = require("express");
const bodyParser = require("body-parser");
const request = require("request");
var cors = require("cors");
var { google } = require("googleapis");
var key = require("./private-key.json");
var MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
var SCOPES = [MESSAGING_SCOPE];
admin.initializeApp({
  credential: admin.credential.cert(key),
  databaseURL: "https://meetapp-33e04.firebaseio.com"
});

var app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(cors());
app.get("/test", (req, res) => {
  res.send({ message: "Test message" });
});
app.get("/token", (req, res) => {
  getAccessToken().then(function(access_token) {
    res.send({ access_token });
  });
});
app.post("/friend/request", (req, res) => {
  let body = req.body;
  if (!body.receiver_key) res.status(400).send("Receiver key missing");
  else if (!body.full_name) res.status(400).send("Full name missing");
  getAccessToken().then(token => {
    admin
      .database()
      .ref(`users/${body.receiver_key}`)
      .on("value", snapshot => {
        let user = snapshot.toJSON();
        request.post(
          "https://fcm.googleapis.com/v1/projects/meetapp-33e04/messages:send",
          {
            auth: {
              bearer: token
            },
            json: {
              message: {
                token: user.token,
                notification: {
                  body: `${body.full_name} sent you friend reqest`,
                  title: "New friend request"
                },
                data: {
                  friend: "new friend reqest"
                }
              }
            }
          },
          data => {}
        );
        res.send("ok");
      });
  });
});
app.post("/new_event", (req, res) => {
  let body = req.body;
  if (!body.key) res.status(400).send("Key missing");
  else if (!body.title) res.status(400).send("Title missing");
  else if (!body.lat) res.status(400).send("Latitude missing");
  else if (!body.lon) res.status(400).send("Longitude missing");
  else if (!body.creator_id) res.status(400).send("Creator id missing");
  getAccessToken().then(token => {
    admin
      .database()
      .ref("users")
      .on("value", snapshot => {
        snapshot.forEach(userSnap => {
          let user = userSnap.toJSON();
          if (user.token || body.creator_id != userSnap.key)
            if (
              CalcDistance(body.lat, body.lon, user.locLat, user.locLon) <= 1500
            ) {
              request.post(
                "https://fcm.googleapis.com/v1/projects/meetapp-33e04/messages:send",
                {
                  auth: {
                    bearer: token
                  },
                  json: {
                    message: {
                      token: user.token,
                      notification: {
                        body: body.title,
                        title: "New event created"
                      },
                      data: {
                        event: body.key
                      }
                    }
                  }
                },
                data => {}
              );
            }
        });
        res.send("ok");
      });
  });
});
function getAccessToken() {
  return new Promise(function(resolve, reject) {
    var jwtClient = new google.auth.JWT(
      key.client_email,
      null,
      key.private_key,
      SCOPES,
      null
    );
    jwtClient.authorize(function(err, tokens) {
      if (err) {
        reject(err);
        return;
      }
      resolve(tokens.access_token);
    });
  });
}
function CalcDistance(lat_a, lng_a, lat_b, lng_b) {
  let pk = 180 / Math.PI;

  let a1 = lat_a / pk;
  let a2 = lng_a / pk;
  let b1 = lat_b / pk;
  let b2 = lng_b / pk;

  let t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
  let t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
  let t3 = Math.sin(a1) * Math.sin(b1);
  let tt = Math.acos(t1 + t2 + t3);

  return 6366000 * tt;
}

exports.api = functions.https.onRequest(app);
