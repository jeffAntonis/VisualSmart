package com.example.gammer.projeto;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Contatos {
    private Context ctx;
    public String numero = "";

    public Contatos(Context contexto){
        this.ctx = contexto;
    }

    public List getContatos(){
        Cursor C_Contatos = this.ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

        //pega os index das colunnas
        int IndexID = C_Contatos.getColumnIndex(ContactsContract.Contacts._ID);
        int IndexTemTelefone = C_Contatos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        int IndexName = C_Contatos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        List Contatos = new ArrayList();

        EntidadeContatos Contato;

        while(C_Contatos.moveToNext()){

            Contato = new EntidadeContatos();
            Contato.setID(C_Contatos.getString(IndexID));            Contato.setNome(C_Contatos.getString(IndexName));
            //verifica se o contato tem telefone
            if(Integer.parseInt(C_Contatos.getString(IndexTemTelefone)) > 0){
                Telefone _Telefone = new Telefone(Contato.getID(), this.ctx);
                Contato.setTelefones(_Telefone.getTelefones());
            }
            Contatos.add(Contato);
        }
        C_Contatos.close();
        return Contatos;
    }


    public String procurarContato(String nome){

        String contatoNome = "";

        Cursor C_Contatos = this.ctx.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

        //pega os index das colunnas
        int IndexID = C_Contatos.getColumnIndex(ContactsContract.Contacts._ID);
        int IndexTemTelefone = C_Contatos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

        int IndexName = C_Contatos.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

        List Contatos = new ArrayList();

        EntidadeContatos Contato;

        while(C_Contatos.moveToNext()){

            String teste = C_Contatos.getString(IndexName);

            if(teste.equalsIgnoreCase(nome)){
                contatoNome = teste;

                procurarNumeroContato(this.ctx, C_Contatos.getString(IndexID));
            }
        }
        return contatoNome;
    }


    public void procurarNumeroContato(Context _ctx, String _IDContato){

        Cursor C_Telefones = _ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _IDContato, null, null);

        int IndexTelefone;
        List Telefones = new ArrayList();

        while(C_Telefones.moveToNext()){

            IndexTelefone = C_Telefones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            numero = C_Telefones.getString(IndexTelefone);

        }

        C_Telefones.close();

    }
}
