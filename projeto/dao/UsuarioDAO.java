package projeto.dao;

import projeto.modelo.Cidade;
import projeto.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements IDAO<Usuario>{
    private static final String ADD_USUARIO = "insert into tb_usuario(nome_usuario,email_usuario,celular_usuario,senha_usuario," +
            "cod_cidade) values(?,?,?,?,?);";
    private static final String REMOVE_USUARIO = "delete from tb_usuario where cod_usuario=?;";
    private static final String UPDATE_USUARIO = "update tb_usuario set nome_usuario=?,email_usuario=?," +
            "celular_usuario=?,senha_usuario=?,cod_cidade=?, ind_stuacao=? " +
            "where cod_usuario=?;";
    private static final String GETALL_USUARIOS_BY_CIDADE = "select * from tb_usuario where cod_cidade=?;";
    private static final String GET_USUARIO_BY_SENHA_EMAIL ="select * from tb_usuario where email_usuario=? and senha_usuario=?;";
    private static final String GETBYID_USUARIO = "select * from tb_usuario where cod_usuario=?;";

    //////////////////////////////////////////////////////////////////////////////////////////////
    private static final String COLUNA_CODIGO = "cod_usuario";
    private static final String COLUNA_NOME = "nome_usuario";
    private static final String COlUNA_EMAIL = "email_usuario";
    private static final String COLUNA_CELULAR = "celular_usuario";
    private static final String COlUNA_SENHA = "senha_usuario";
    private static final String COLUNA_SITUACAO = "ind_situacao";
    private static final String COLUNA_CODIGO_CIDADE = "cod_cidade";
    private Connection conexao;

    public UsuarioDAO(Connection connection){
        this.conexao = connection;
    }

    @Override
    public void add(Usuario usuario) {
        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(ADD_USUARIO);

            preparedStatement.setString(1,usuario.getNome());
            preparedStatement.setString(2,usuario.getEmail());
            preparedStatement.setString(3,usuario.getCelular());
            preparedStatement.setString(4,usuario.getSenha());
            preparedStatement.setInt(5,usuario.getCidade().getCodigo());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Usuario usuario) {
        remove(usuario.getCodigo());
    }

    @Override
    public void remove(int id) {
        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(REMOVE_USUARIO);

            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Usuario usuario) {

        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(UPDATE_USUARIO);

            preparedStatement.setString(1,usuario.getNome());
            preparedStatement.setString(2,usuario.getEmail());
            preparedStatement.setString(3,usuario.getCelular());
            preparedStatement.setString(4,usuario.getSenha());
            preparedStatement.setInt(5,usuario.getCidade().getCodigo());
            preparedStatement.setString(6,usuario.getSituacao());
            preparedStatement.setInt(7,usuario.getCodigo());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Usuario> getAll() {
        List<Usuario> usuarios = new ArrayList<>();

        try {

            CidadeDAO cidadeDAO = new CidadeDAO(conexao);
            List<Cidade> cidades = cidadeDAO.getAll();
            PreparedStatement preparedStatement = conexao.prepareStatement(GETALL_USUARIOS_BY_CIDADE);

            for (Cidade c : cidades) {
                preparedStatement.setInt(1, c.getCodigo());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Usuario usuarioAux = new Usuario();

                    usuarioAux.setNome(resultSet.getString(COLUNA_NOME));
                    usuarioAux.setEmail(resultSet.getString(COlUNA_EMAIL));
                    usuarioAux.setCelular(resultSet.getString(COLUNA_CELULAR));
                    usuarioAux.setSenha(resultSet.getString(COlUNA_SENHA));
                    usuarioAux.setSituacao(resultSet.getString(COLUNA_SITUACAO));
                    usuarioAux.setCodigo(resultSet.getInt(COLUNA_CODIGO));
                    usuarioAux.setCidade(c);

                    usuarios.add(usuarioAux);
                }

                resultSet.close();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    @Override
    public Usuario getById(int id) {

        Usuario usuarioBuscado = null;

        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(GETBYID_USUARIO);

            preparedStatement.setInt(1,id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                usuarioBuscado = new Usuario();
                CidadeDAO cidadeDAO = new CidadeDAO(conexao);
                Cidade cidade = cidadeDAO.getById(resultSet.getInt(COLUNA_CODIGO_CIDADE));

                usuarioBuscado.setNome(resultSet.getString(COLUNA_NOME));
                usuarioBuscado.setEmail(resultSet.getString(COlUNA_EMAIL));
                usuarioBuscado.setCelular(resultSet.getString(COLUNA_CELULAR));
                usuarioBuscado.setSenha(resultSet.getString(COlUNA_SENHA));
                usuarioBuscado.setSituacao(resultSet.getString(COLUNA_SITUACAO));
                usuarioBuscado.setCodigo(resultSet.getInt(COLUNA_CODIGO));
                usuarioBuscado.setCidade(cidade);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarioBuscado;
    }

    public Usuario getUsuarioByEmailSenha(String email, String senha){
        Usuario usuarioBuscado = null;

        try{
            PreparedStatement preparedStatement = conexao.prepareStatement(GET_USUARIO_BY_SENHA_EMAIL);

            preparedStatement.setString(1,email);
            preparedStatement.setString(2,senha);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                usuarioBuscado = new Usuario();
                CidadeDAO cidadeDAO = new CidadeDAO(conexao);
                Cidade cidade = cidadeDAO.getById(resultSet.getInt(COLUNA_CODIGO_CIDADE));

                usuarioBuscado.setNome(resultSet.getString(COLUNA_NOME));
                usuarioBuscado.setEmail(resultSet.getString(COlUNA_EMAIL));
                usuarioBuscado.setCelular(resultSet.getString(COLUNA_CELULAR));
                usuarioBuscado.setSenha(resultSet.getString(COlUNA_SENHA));
                usuarioBuscado.setSituacao(resultSet.getString(COLUNA_SITUACAO));
                usuarioBuscado.setCodigo(resultSet.getInt(COLUNA_CODIGO));
                usuarioBuscado.setCidade(cidade);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarioBuscado;
    }
}