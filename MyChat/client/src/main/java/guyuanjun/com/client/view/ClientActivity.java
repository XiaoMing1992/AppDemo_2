package guyuanjun.com.client.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import guyuanjun.com.client.R;
import guyuanjun.com.client.presenter.IPresenter;
import guyuanjun.com.client.presenter.PresenterComp;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener, IView{

    private Button send;
    private EditText input;

    private IPresenter iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        initView();
        init();
    }

    private void initView(){
        input = (EditText)findViewById(R.id.input);
        send = (Button)findViewById(R.id.send);
        send.setOnClickListener(this);
    }

    private void init(){
        iPresenter = new PresenterComp(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
                iPresenter.sengMsg(input.getText().toString().trim());
                Log.d("client", input.getText().toString().trim());

//                boolean res = iPresenter.sengMsg(input.getText().toString().trim());
//                if (res) {
//                    iPresenter.clear();
//                }
                iPresenter.clear();
                break;
        }
    }

    @Override
    public void onClearText() {
        Toast.makeText(ClientActivity.this, input.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}
