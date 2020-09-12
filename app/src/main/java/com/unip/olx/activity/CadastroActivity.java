package com.unip.olx.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.unip.olx.R;
import com.unip.olx.helper.ConfiguracaoFirebase;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        incializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                
                if(!email.isEmpty())
                {
                  if(!senha.isEmpty())
                  {
                     if(tipoAcesso.isChecked())
                      {
                          // cadastro
                          autenticacao.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {

                                  if(task.isSuccessful())
                                  {
                                      Toast.makeText(CadastroActivity.this, "Cadastro Realizado com Sucesso.", Toast.LENGTH_SHORT).show();
                                  }
                                  else
                                  {
                                      String erroExcecao;
                                      try
                                      {
                                          throw task.getException();
                                      }
                                      catch (FirebaseAuthWeakPasswordException e)
                                      {
                                          erroExcecao = "Digite uma Senha Mais Forte.";
                                      }
                                      catch (FirebaseAuthInvalidUserException e)
                                      {
                                          erroExcecao = "Por Favor, Digite um E-mail Válido.";
                                      }
                                      catch (FirebaseAuthUserCollisionException e)
                                      {
                                          erroExcecao = "Este Conta Já Foi Cadastrada.";
                                      }
                                      catch (Exception e)
                                      {
                                          erroExcecao = "Ao Cadastrar o Usuário: " + e.getMessage();
                                          e.printStackTrace();
                                      }

                                      Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });
                      }
                      else
                      {
                        //  login
                          autenticacao.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                              @Override
                              public void onComplete(@NonNull Task<AuthResult> task) {
                                  
                                  if(task.isSuccessful())
                                  {
                                      Toast.makeText(CadastroActivity.this, "Logado com Sucesso.", Toast.LENGTH_SHORT).show();
                                      startActivity(new Intent(getApplicationContext(), AnunciosActivity.class));
                                  }
                                  else
                                  {
                                      Toast.makeText(CadastroActivity.this, "Erro ao Tentar Fazer Login: " + Objects.requireNonNull(task.getException()).toString() , Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });
                      }
                  }
                  else
                  {
                      Toast.makeText(CadastroActivity.this, "Preencha o Campo Senha.",
                              Toast.LENGTH_SHORT).show();
                  }
                }
                else
                { Toast.makeText(CadastroActivity.this, "Preencha o Campo Email.",
                                 Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void incializaComponentes()
    {
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
    }
}