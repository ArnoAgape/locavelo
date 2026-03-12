const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { setGlobalOptions } = require("firebase-functions/v2");
const admin = require("firebase-admin");

admin.initializeApp();

setGlobalOptions({ maxInstances: 10 });

exports.sendMessageNotification = onDocumentCreated(
  "conversations/{conversationId}/messages/{messageId}",
  async (event) => {

    const message = event.data.data();
    const conversationId = event.params.conversationId;

    const senderId = message.senderId;

    // récupérer la conversation
    const conversationDoc = await admin.firestore()
      .collection("conversations")
      .doc(conversationId)
      .get();

    const conversation = conversationDoc.data();

    const participants = conversation.participants;

    // trouver le destinataire
    const receiverId = participants.find(id => id !== senderId);

    const userDoc = await admin.firestore()
      .collection("users")
      .doc(receiverId)
      .get();

    const token = userDoc.data()?.fcmToken;

    if (!token) {
      console.log("No FCM token for user");
      return;
    }

    const payload = {
      notification: {
        title: "Nouveau message",
        body: message.text
      },
      data: {
        conversationId: conversationId
      }
    };

    await admin.messaging().sendToDevice(token, payload);
  }
);