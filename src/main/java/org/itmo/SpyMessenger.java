package org.itmo;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class SpyMessenger {
    private final Map<String, List<Message>> users = new HashMap<>();

    public SpyMessenger() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            for (Map.Entry<String, List<Message>> entry : users.entrySet()) {
                entry.getValue().removeIf(message -> System.currentTimeMillis() - message.timestamp() > 1500);
            }
        }, 1, TimeUnit.MICROSECONDS);
    }

    void sendMessage(String sender, String receiver, String message, String passcode) {
        Message m = new Message(message, System.currentTimeMillis(), sender, passcode);
        users.computeIfAbsent(receiver, k -> new ArrayList<>()).add(m);
        if (users.get(receiver).size() == 6) {
            users.get(receiver).removeFirst();
        }
    }

    String readMessage(String user, String passcode) {
        if (users.containsKey(user)) {
            Message message = users.get(user).stream().filter(msg -> msg.password().equals(passcode)).findFirst().get();
            String text = message.text();
            users.get(user).remove(message);
            return text;
        }
        return null;
    }
}
