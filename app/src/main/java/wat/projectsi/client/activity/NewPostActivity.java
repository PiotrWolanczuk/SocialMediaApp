package wat.projectsi.client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import wat.projectsi.R;

public class NewPostActivity extends AppCompatActivity {

    Button sendButton;
    Button addPhoto;
    EditText newPostText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        sendButton = findViewById(R.id.send_button);
        addPhoto = findViewById(R.id.add_photo_button);
        newPostText = findViewById(R.id.message_text);


    }

    public void send_new_post(View view) {
        StringBuilder messageText = new StringBuilder();

        messageText.append(newPostText.getText());
    }

    public void add_photo_from_gallery(View view) {

    }
}
