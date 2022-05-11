package service;

import com.google.gson.*;
import model.Epic;
import model.Subtask;
import model.Task;

import java.lang.reflect.Type;

public class TaskSerializer implements JsonSerializer<Task> {

    Gson gson = new Gson();

        @Override
        public JsonElement serialize(final Task task, final Type type, final JsonSerializationContext context) {
            JsonElement json = gson.toJsonTree(task);
            if(task instanceof Task)
                json.getAsJsonObject().addProperty("type", "task");
            if(task instanceof Epic)
                json.getAsJsonObject().addProperty("type", "epic");
            if(task instanceof Subtask)
                json.getAsJsonObject().addProperty("type", "subtask");
            return json;
        }
}

