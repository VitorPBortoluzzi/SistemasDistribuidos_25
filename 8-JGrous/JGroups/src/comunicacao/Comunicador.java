package comunicacao;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class Comunicador extends ReceiverAdapter {

    JChannel channel;
    List<Address> listaMembros;
    String frase;
    Message mensagem;
    JFrame_chatJGROUPS meuFrame;
    StringBuffer membrosStringBuffer;

    // 2 Notificar
    List<Address> listaAnterior = null;

    // 3 Historico Msg
    // StringBuffer historicoMensagens = new StringBuffer();
    StringBuffer historicoMensagensPublicas = new StringBuffer();
    StringBuffer historicoMensagensPrivadas = new StringBuffer();

    public void iniciar(JFrame_chatJGROUPS meuFrame) throws Exception {

        System.setProperty("java.net.preferIPv4Stack", "true");//desabilita ipv6, para que só sejam aceitas conexões via ipv4
        System.setProperty("jgroups.bind_addr", "127.0.0.1");
        /*
         * JGroups utiliza um JChannel como principal forma de conectar
         * a um cluster/grupo. É atraves dele que enviaremos e recebermos mensagens
         * bem como registrar os eventos callback quando acontecer alguma
         * mudança (por exemplo, entrada de um membro no grupo).
         * 
         * Neste caso, criamos uma instancia deste objeto, utilizando configurações default.
         */
        this.channel = new JChannel();
        /*
         * Definimos através do método setReceiver qual classe implementará
         * o método callback receive, que será chamado toda vez que alguém
         * enviar uma mensagem ao cluster/grupo. Neste caso, a própria classe
         * implementa o método receive mais abaixo.
         */
        this.channel.setReceiver(this);
        /*
         * O método connect faz com que este processo entre no cluster/grupo ChatCluster.
         * Não há a necessidade de se criar explicitamente um cluster, pois o método connect
         * cria o cluster caso este seja o primeiro membro a entrar nele.
         */
        this.meuFrame = meuFrame;

        this.channel.setName(meuFrame.getjTextField_apelido().getText());
        this.channel.connect(meuFrame.getTitle());
        this.channel.getState(null, 10000); // espera até 10s pelo estado
        this.meuFrame.getjTextArea_listaMembros().setText(membrosStringBuffer.toString());
    }

public void enviar(String frase, String participante) {
    try {
        if (participante == null) {
            // Mensagem pública
            this.mensagem = new Message(null, frase);
            this.channel.send(this.mensagem);
        } else {
            // Mensagem privada
            for (Address addr : this.listaMembros) {
                if (participante.equals(addr.toString())) {
                    this.mensagem = new Message(addr, frase);
                    this.channel.send(this.mensagem);

                    // Exibir localmente também
                    String linha = "[Privado para " + participante + "] Você disse: " + frase + "\n";
                    this.meuFrame.getjTextArea_mensagensGerais().append(linha);

                    // Armazena no histórico de privadas do remetente
                    synchronized (historicoMensagensPrivadas) {
                        historicoMensagensPrivadas.append(linha);
                    }
                    break;
                }
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(meuFrame, "Algo ocorreu de errado ao enviar sua mensagem!!");
    }
}

    public void finalizar() {
        this.channel.close();
    }

    /*
     * O método abaixo é callback, e é chamado toda vez que alguem
     * envia uma mensagem ao processo/grupo/canal. Esta mensagem é recebida no parâmetro
     * Message msg. Nessa implementação, mostramos na tela o originador
     * da mensagem em msg.getSrc() e a mensagem propriamente dita em
     * msg.getObject
     */
    @Override
    public void receive(Message msg) {
        Date dt = new Date();
        String linha = "[" + dt.toString() + "] " + msg.getSrc() + " disse: "
                + msg.getObject().toString() + "\n";

        //historicoMensagens.append(linha);
        if (msg.getDest() == null) {
            // Mensagem pública
            historicoMensagensPublicas.append(linha);
        } else {
            // Mensagem privada
            historicoMensagensPrivadas.append(linha);
        }
        this.meuFrame.getjTextArea_mensagensGerais().append(linha);
    }

    /*@Override
    public void getState(java.io.OutputStream output) throws Exception {
        synchronized (historicoMensagens) {
            output.write(historicoMensagens.toString().getBytes());
        }
    }
     */
    @Override
    public void getState(java.io.OutputStream output) throws Exception {
        synchronized (historicoMensagensPublicas) {
            output.write(historicoMensagensPublicas.toString().getBytes());
        }
    }

    @Override
    public void setState(java.io.InputStream input) throws Exception {
        byte[] buf = input.readAllBytes();
        String estado = new String(buf);

        synchronized (historicoMensagensPublicas) {
            historicoMensagensPublicas = new StringBuffer(estado);
        }

        this.meuFrame.getjTextArea_mensagensGerais().setText(estado);
    }

    /*
     * O método abaixo é callback, e é chamado toda vez que uma nova
     * instancia entra no grupo, ou se alguma instancia sai do grupo.
     * Ele recebe uma View como parâmetro. Este objeto possui informações
     * sobre todos os membros do grupo.
     * Na nossa implementação, quando damos um print no objeto new_view
     * ele mostra, respectivamente:
     *      [Criador do grupo | ID da View]  [Membros do grupo]
     * 
     * Cada View possui uma ID, que a identifica. 
     * O ID da View é um Relógio de Lamport que marca a ocorrência de eventos.
     */
    @Override
    public void viewAccepted(View view_atual) {
        List<Address> novaLista = view_atual.getMembers();

        // Detectar entradas/saídas
        if (listaAnterior != null) {
            for (Address antigo : listaAnterior) {
                if (!novaLista.contains(antigo)) {
                    this.meuFrame.getjTextArea_mensagensGerais()
                            .append("[INFO] " + antigo + " saiu do grupo.\n");
                }
            }
            for (Address novo : novaLista) {
                if (!listaAnterior.contains(novo)) {
                    this.meuFrame.getjTextArea_mensagensGerais()
                            .append("[INFO] " + novo + " entrou no grupo.\n");
                }
            }
        }

        /**
         * Percorre a lista de membros conectados ao grupo JGroups e extrai
         * informações de cada um (nome e endereço IP). Essas informações são:
         *
         * 1. Adicionadas a um StringBuffer (`membrosStringBuffer`) para
         * exibição textual. 2. Inseridas em um componente gráfico (JComboBox)
         * na interface, permitindo a seleção de participantes do grupo.
         *
         * O método utiliza verificação de tipo (`instanceof`) para identificar
         * se o objeto de endereço é uma instância de
         * `org.jgroups.stack.IpAddress` — a classe responsável por representar
         * endereços IP em JGroups.
         *
         * Caso seja, o IP é extraído e convertido em texto com
         * `getHostAddress()`. Caso contrário, é atribuído o valor "n/a" (não
         * aplicável).
         *
         */
        this.listaMembros = novaLista;
        this.membrosStringBuffer = new StringBuffer();
        this.meuFrame.getjTextArea_listaMembros().setText("");
        this.meuFrame.getjComboBox_listaParticipantesGrupo().removeAllItems();
        this.meuFrame.getjComboBox_listaParticipantesGrupo().addItem("Selecione o participante");

        for (Address addr : listaMembros) {
            String nome = addr.toString();
            String ip = "n/a";

            try {
                // Tenta pegar o endereço físico associado ao endereço lógico
                Address physicalAddr = (Address) channel.down(
                        new org.jgroups.Event(org.jgroups.Event.GET_PHYSICAL_ADDRESS, addr)
                );

                if (physicalAddr instanceof org.jgroups.stack.IpAddress) {
                    org.jgroups.stack.IpAddress ipAddr = (org.jgroups.stack.IpAddress) physicalAddr;
                    String host = ipAddr.getIpAddress().getHostAddress();
                    int port = ipAddr.getPort();
                    ip = host + ":" + port; // exibe IP + porta
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            membrosStringBuffer.append(nome + " (" + ip + ")\n");
            this.meuFrame.getjComboBox_listaParticipantesGrupo().addItem(nome);
        }

        this.meuFrame.getjTextArea_listaMembros().setText(membrosStringBuffer.toString());
        listaAnterior = novaLista;
    }

    /*
     * Este método callback é chamado toda vez que um membro é 
     * suspeito de ter falhado, porém ainda não foi excluído
     * do grupo. Esse método só é executado no coordenador do grupo.
     */
    @Override
    public void suspect(Address mbr) {
        JOptionPane.showMessageDialog(meuFrame, "PROCESSO SUSPEITO DE FALHA: " + mbr);
    }

}
