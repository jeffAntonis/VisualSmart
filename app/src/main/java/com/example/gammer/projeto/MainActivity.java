package com.example.gammer.projeto;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;
import com.example.gammer.projeto.SwipeEvents;


public class MainActivity extends Activity implements TextToSpeech.OnInitListener{

    private TextToSpeech textToSpeech;
    private Context context;

    private final int ID_TEXTO_PARA_VOZ = 100;
    static final int REQUEST_SELECT_PHONE_NUMBER = 1;

    private static final int PERMISSIONS_REQUEST_CALL_PHONE = 80;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 60;

    public String funcionalidade = "";
    public String ditado = "";
    public String confirmou = "";
    public String tela = "";
    public String contato = "";
    public String telefone = "";
    public String temContato = "";
    public String mensagem = "";
    public String pagina = "";

    public String clsPhonename = null;
    public String clsphoneNo = null;

    private GridLayout gridLayout;
    int i = 0;
    int menu = 0;
    private String falar;
    private List ListaContatos = new ArrayList();
    private Contatos Contato = new Contatos(this);

    RelativeLayout swipeElementRelactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //INSTANCIANDO CLASSES
        context = getApplicationContext();
        textToSpeech = new TextToSpeech( this, this);

        //TRATANDO OS IDS
        gridLayout = (GridLayout) findViewById( R.id.mainGrid );
        swipeElementRelactive = (RelativeLayout) findViewById( R.id.swipeElementRelactive );
        CardView telefone = (CardView) findViewById( R.id.telefone );
        CardView internet = (CardView) findViewById( R.id.internet );
        CardView localizacao = (CardView) findViewById( R.id.localizacao );
        CardView mensagem = (CardView) findViewById( R.id.mensagem );


        //CHAMADA DE FUNCOES
        //setSingleEvent( gridLayout );
        swipeElementRelactive.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {

                //showToast("cima");
                acessarFuncionalidade();

            }
            public void onSwipeRight() {

                //showToast("direita");
                if(menu == 4) {
                    menu = 1;
                } else{
                    menu++;
                }
                falarFuncionalidade(menu);
            }
            public void onSwipeLeft() {

                //showToast("esquerda");
                if(menu == 1){
                    menu = 4;
                } else{
                    menu--;
                }
                falarFuncionalidade(menu);
            }
            public void onSwipeBottom() {

                //showToast("baixo");
            }

        });

        //===================================ONCLICKS======================================

        //TELEFONE
        telefone.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {

                        if (i == 1) {
                            textoVoz("Telefone");

                        } else if (i == 2) {
                            funcionalidade = "telefone";
                            verificaSeTemContato( null );
                        }
                        i = 0;
                    }
                }, 500 );

            }
        } );

        //MENSAGEM
        mensagem.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {

                        if (i == 1) {
                            textoVoz("Mensagem");

                        } else if (i == 2) {
                            funcionalidade = "mensagem";
                            verificaSeTemContato( null );
                        }
                        i = 0;
                    }
                }, 500 );

            }
        } );

        //INTERNET
        internet.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {

                        if (i == 1) {
                            textoVoz("Internet");

                        } else if (i == 2) {
                            opcaoInternet(null);
                        }
                        i = 0;
                    }
                }, 500 );

            }
        } );

        //LOCALIZACAO
        localizacao.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view) {
                i++;
                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {

                        if (i == 1) {
                            textoVoz("Localização");

                        } else if (i == 2) {
                            textoVoz("Funcionalidade em desenvolvimento");
                        }
                        i = 0;
                    }
                }, 500 );

            }
        } );

    }

    @Override
    public void onInit(int status){
        if(status == TextToSpeech.SUCCESS){
            int result  = textToSpeech.setLanguage(Locale.getDefault());

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText( this, "Linguagem não suportada", Toast.LENGTH_SHORT).show();

            }
        } else{
            Toast.makeText( this, "Text To Speech Erro!", Toast.LENGTH_SHORT).show();
        }
    }

    // we are setting onClickListener for each element
    private void setSingleEvent(GridLayout gridLayout) {
        for (i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt( i );
            final int finalI = i;

            cardView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i++;

                    Handler handler = new Handler();
                    handler.postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            //Difere qual a grid que o usuario clicou
                            switch (finalI) {

                                case 0:
                                    falar = "Telefone";
                                    break;
                                case 1:
                                    falar = "Internet";
                                    break;
                                case 2:
                                    falar = "Localização";
                                    break;
                                case 3:
                                    falar = "Mensagem";
                                    break;
                            }

                            if (i == 1) {
                                Toast.makeText( MainActivity.this, "Um Click " + falar,
                                        Toast.LENGTH_SHORT ).show();

                                textoVoz( falar );

                            } else if (i == 2) {
                                Toast.makeText( MainActivity.this, "Dois Clicks " + falar,
                                        Toast.LENGTH_SHORT ).show();


                                if (finalI == 3) {
                                    funcionalidade = "mensagem";
                                    verificaSeTemContato( null );
                                } else if (finalI == 0) {
                                    funcionalidade = "telefone";
                                    verificaSeTemContato( null );
                                } else if(finalI == 1){
                                    opcaoInternet(null);
                                } else if(finalI == 2){

                                    textoVoz("Funcionalidade em desenvolvimento");
                                }
                            }
                            i = 0;


                        }
                    }, 500 );

                }
            } );
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    //VERIFICA SE EXISTE CONTATO
    public void verificaSeTemContato(String opcao) {

        //FUNCIONALIDADE TELEFONE
        if(funcionalidade.equals("telefone")){
            try {
                if (opcao == null) {
                    tela = "temContato";
                    Toast.makeText( getApplicationContext(), "Existe Contato?", Toast.LENGTH_SHORT ).show();
                    textoVoz( "Se existir o contato em seu celular diga sim, caso não tenha diga não!" );
                    sleep( 5000 );

                    pedirParaFalar();
                } else if (opcao == "falouSeTemContato") {
                    if (temContato.equals( "sim" )) {
                        tela = "confirmouTemContato";
                        textoVoz( "Voce disse que tem contato!" );
                        Toast.makeText( getApplicationContext(), "Disse que tem", Toast.LENGTH_SHORT ).show();
                        sleep( 3000 );
                        textoVoz( "Fale o nome do contato" );
                        sleep( 3000 );
                        pedirParaFalar();

                    } else if (temContato.equals( "não" )) {
                        tela = "naoTemContato";
                        textoVoz( "Voce disse que não tem contato!" );
                        Toast.makeText( getApplicationContext(), "Disse que não", Toast.LENGTH_SHORT ).show();
                        sleep( 3000 );
                        textoVoz( "Fale o numero do celular" );
                        sleep( 3000 );
                        pedirParaFalar();
                    }
                } else if(opcao.equals("falouNome")){
                    tela = "falouContato";
                    textoVoz( "Voce falou "+contato );
                    sleep( 3000 );
                    textoVoz( "Irei procurar o numero em sua agenda, aguarde um isntante");
                    sleep( 4000 );
                    testeContatos();
                    sleep( 2000 );
                    textoVoz( "O numero do seu contato é o : "+telefone.toString());
                    sleep( 6000 );
                    textoVoz( "Se o numero estiver certo diga sim, caso contrário diga não!");
                    sleep( 5000 );
                    pedirParaFalar();
                } else if(opcao.equals("confirmarNumeroContato")){

                    if(confirmou.equals("sim")){
                        textoVoz( "Voce confirmou o numero do contato" );
                        sleep( 3000 );
                        textoVoz( "Aguarde a ligação iniciar" );
                        sleep( 3000 );
                        realizarLigacao();
                    } else if(confirmou.equals("não")){
                        textoVoz( "Voce negou o numero do contato" );
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        //FUNCIONALIDADE MENSAGEM
        } else if(funcionalidade.equals("mensagem")){

            try {
                if (opcao == null) {
                    tela = "temContato";
                    Toast.makeText( getApplicationContext(), "Existe Contato?", Toast.LENGTH_SHORT ).show();
                    textoVoz( "Se existir o contato em seu celular diga sim, caso não tenha diga não!" );
                    sleep( 5000 );

                    pedirParaFalar();
                } else if (opcao == "falouSeTemContato") {
                    if (temContato.equals( "sim" )) {
                        tela = "confirmouTemContato";
                        textoVoz( "Voce disse que tem contato!" );
                        Toast.makeText( getApplicationContext(), "Disse que tem", Toast.LENGTH_SHORT ).show();
                        sleep( 3000 );
                        textoVoz( "Fale o nome do contato" );
                        sleep( 3000 );
                        pedirParaFalar();

                    } else if (temContato.equals( "não" )) {
                        tela = "naoTemContato";
                        textoVoz( "Voce disse que não tem contato!" );
                        Toast.makeText( getApplicationContext(), "Disse que não", Toast.LENGTH_SHORT ).show();
                        sleep( 3000 );
                        textoVoz( "Fale o numero do celular" );
                        sleep( 3000 );
                        pedirParaFalar();
                    }
                } else if(opcao.equals("falouNome")){
                    tela = "falouContato";
                    textoVoz( "Voce falou "+contato );
                    sleep( 3000 );
                    textoVoz( "Irei procurar o numero em sua agenda, aguarde um isntante");
                    sleep( 4000 );
                    testeContatos();
                    sleep( 2000 );
                    if(!telefone.isEmpty()){
                        textoVoz( "O numero do seu contato é o : "+telefone.toString());
                        sleep( 6000 );
                    } else {
                        textoVoz( "Só mais um instante, irei procurar o contato, em seu cartão de memória!");
                        selecionarContatoCartao();
                        sleep( 7000 );
                        textoVoz( "O numero do seu contato é o : "+telefone.toString());
                        sleep( 6000 );
                    }
                    textoVoz( "Se o numero estiver certo diga sim, caso contrário diga não!");
                    sleep( 5000 );
                    pedirParaFalar();
                } else if(opcao.equals("confirmarNumeroContato")){

                    if(confirmou.equals("sim")){
                        tela = "mensagem";
                        textoVoz( "Voce confirmou o numero do contato" );
                        sleep( 4000 );
                        textoVoz( "Agora fale a mensagem" );
                        sleep( 3000 );
                        pedirParaFalar();
                    } else if(confirmou.equals("não")){
                        textoVoz( "Voce negou o numero do contato" );
                    }
                } else if(opcao.equals("mensagemFalouNumero")){
                    tela = "mensagem";
                    textoVoz( "Agora fale a mensagem" );
                    sleep( 3000 );
                    pedirParaFalar();

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }

    //FUNÇÃO SELECIONAR CONTATO
    public void selecionarContato() {


        try {
            String[] PROJECTION = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
            };

            Cursor c = managedQuery( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null );
            if (c.moveToFirst()) {

                do {
                    clsPhonename = c.getString( c.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME ) );
                    clsphoneNo = c.getString( c.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER ) );

                    clsphoneNo.replaceAll( "\\D", "" );
                    clsPhonename = clsPhonename.replaceAll( "&", "" );
                    clsPhonename.replace( "|", "" );
                    String clsPhoneName = clsPhonename.replace( "|", "" );

                    /*
                    Toast.makeText(MainActivity.this,"Contato: "+ clsPhoneName +" Numero: "+clsphoneNo,
                            Toast.LENGTH_SHORT).show();
                     */

                    if (clsPhoneName.equals( contato )) {
                        Toast.makeText( MainActivity.this, "Contato encontrado",
                                Toast.LENGTH_SHORT ).show();

                        telefone = clsphoneNo;
                        break;
                    }

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //SELECIONAR COTNATO CARTAO
    public void selecionarContatoCartao() {
        try {
            String clsSimPhonename = null;
            String clsSimphoneNo = null;

            Uri simUri = Uri.parse("content://icc/adn");
            Cursor cursorSim = this.getContentResolver().query(simUri, null,
                    null, null, null);

            while (cursorSim.moveToNext()) {
                clsSimPhonename = cursorSim.getString(cursorSim
                        .getColumnIndex("name"));
                clsSimphoneNo = cursorSim.getString(cursorSim
                        .getColumnIndex("number"));
                clsSimphoneNo.replaceAll("\\D", "");
                clsSimphoneNo.replaceAll("&", "");
                clsSimPhonename = clsSimPhonename.replace("|", "");
                //System.out.println("SimContacts" + clsSimPhonename);
                //System.out.println("SimContactsNo" + clsSimphoneNo);

                if (clsSimPhonename.equals( contato )) {
                    Toast.makeText( MainActivity.this, "Contato encontrado",
                            Toast.LENGTH_SHORT ).show();

                    telefone = clsSimphoneNo;
                    break;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pedirParaFalar() {

        Toast.makeText( getApplicationContext(), "Entrada da função pedir para falar ",
                Toast.LENGTH_SHORT ).show();


        Intent iVoz = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );

        iVoz.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );

        iVoz.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );


        try {
            startActivityForResult( iVoz, ID_TEXTO_PARA_VOZ );
        } catch (ActivityNotFoundException a) {
            Toast.makeText( getApplicationContext(), "Seu celular não suporta o comando de voz",
                    Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    protected void onActivityResult(int id, int resultCodeID, Intent dados) {
        super.onActivityResult( id, resultCodeID, dados );

        switch (id) {

            case ID_TEXTO_PARA_VOZ:
                if (resultCodeID == RESULT_OK && dados != null) {
                    ArrayList<String> result = dados.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS );
                    ditado = result.get( 0 );

                    Toast.makeText( getApplicationContext(), ditado, Toast.LENGTH_SHORT ).show();

                    //FUINCIONALIDADE TELEFONE
                    if(funcionalidade.equals("telefone")) {
                        if (tela.equals( "confirmouTemContato" )) {
                            contato = ditado;
                            verificaSeTemContato( "falouNome" );
                        } else if (tela.equals( "confirmar" )) {
                            confirmou = ditado;
                        } else if (tela.equals( "temContato" )) {
                            temContato = ditado;
                            verificaSeTemContato( "falouSeTemContato" );

                        } else if (tela.equals( "naoTemContato" )) {
                            telefone = ditado;
                            realizarLigacao();
                        } else if (tela.equals( "falouContato" )) {
                            confirmou = ditado;
                            verificaSeTemContato( "confirmarNumeroContato" );
                        }

                        //FUINCIONALIDADE MENSAGEM
                    } else if(funcionalidade.equals("mensagem")){
                        if (tela.equals( "confirmouTemContato" )) {
                            contato = ditado;
                            verificaSeTemContato( "falouNome" );
                        } else if (tela.equals( "confirmar" )) {
                            confirmou = ditado;
                        } else if (tela.equals( "temContato" )) {
                            temContato = ditado;
                            verificaSeTemContato( "falouSeTemContato" );

                        } else if (tela.equals( "naoTemContato" )) {
                            telefone = ditado;
                            verificaSeTemContato( "mensagemFalouNumero" );
                        } else if (tela.equals( "falouContato" )) {
                            confirmou = ditado;
                            verificaSeTemContato( "confirmarNumeroContato" );
                        } else if(tela.equals("mensagem")){
                            mensagem = ditado;
                            mandarMensagem();
                        }
                    } else{
                        if(tela.equals( "opcaoInternet" )){
                            pagina = ditado;
                            opcaoInternet("falouPesquiar");
                        }
                    }

                }
                break;
        }
    }

    public void textoVoz(String texto) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak( texto.toString(), TextToSpeech.QUEUE_FLUSH, null, null );
        } else {
            textToSpeech.speak( texto.toString(), TextToSpeech.QUEUE_FLUSH, null );
        }
    }


    public void realizarLigacao() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission( Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
        } else{
            Uri uri = Uri.parse( "tel:" + telefone );
            Intent intent = new Intent( Intent.ACTION_CALL, uri );
            startActivity( intent );
        }

    }

    //OPCAO MENSAGEM
    public void mandarMensagem(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission( Manifest.permission.SEND_SMS ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
        } else{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
        }

    }

    //OPCAO INTERNET
    public void opcaoInternet(String opcao){

        try {
            if(opcao == null){
                tela = "opcaoInternet";
                textoVoz("Olá, o que você deseja pesquisar?");
                sleep(5000 );
                pedirParaFalar();

            } else if(opcao.equals("falouPesquiar")){
                textoVoz("Voce quis pesquisar, sobre, "+pagina);
                sleep(5000 );
                textoVoz("Aguarde, irei pesquisar para você!");
                sleep(5000 );
                navegar();

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void navegar(){

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra( SearchManager.QUERY, pagina);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void testeContatos(){

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            //Após este ponto, você espera pelo retorno de chamada no método overriden onRequestPermissionsResult
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            // Versão do Android é menor que 6.0 ou a permissão já é concedida.

            String retorno = Contato.procurarContato(contato);
            String iae = Contato.numero;
            telefone = iae;
            Toast.makeText(getApplicationContext(), "Nome: "+retorno, Toast.LENGTH_SHORT ).show();
            Toast.makeText(getApplicationContext(), "Numero: "+iae, Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissão concedida
                    testeContatos();
                } else {
                    Toast.makeText(this, "\n" +
                            "Até você conceder a permissão, não podemos exibir os nomes", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissão concedida
                    realizarLigacao();
                } else {
                    Toast.makeText(this, "\n" +
                            "Até você conceder a permissão, não podemos exibir os nomes", Toast.LENGTH_SHORT).show();
                }
                break;

            case PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permissão concedida
                    mandarMensagem();
                } else {
                    Toast.makeText(this, "\n" +
                            "Até você conceder a permissão, não podemos exibir os nomes", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //SHOW TOAST
    public void showToast( String message ){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    //FALAR FUNCIONALIDADE
    public void falarFuncionalidade(int a){

        switch (a){
            case 1:
                textoVoz("Telefone");
                break;
            case 2:
                textoVoz("Internet");
                break;

            case 3:
                textoVoz("Localização");
                break;
            case 4:
                textoVoz("Mensagem");
                break;
        }
    }

    //ACESSAR FUNCIONALIDADE
    public void acessarFuncionalidade(){

        switch (menu){
            case 1:
                funcionalidade = "telefone";
                verificaSeTemContato( null );
                break;
            case 2:
                opcaoInternet(null);
                break;

            case 3:
                textoVoz("Funcionalidade em desenvolvimento");
                break;
            case 4:
                funcionalidade = "mensagem";
                verificaSeTemContato( null );
                break;
        }
    }

}

