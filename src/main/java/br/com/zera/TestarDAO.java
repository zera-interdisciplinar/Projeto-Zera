package br.com.zera;

import br.com.zera.dao.OrganizacaoDAO;
import br.com.zera.model.Organizacao;

import java.time.LocalDate;
import java.util.List;

public class TestarDAO {
    public static void main(String[] args) {
        OrganizacaoDAO dao = new OrganizacaoDAO();

        // 1. Testar INSERT
        Organizacao nova = new Organizacao(0, "12345678000199", "Empresa Teste", LocalDate.now());
        dao.insert(nova);
        System.out.println("Inserido com sucesso!");

        // 2. Testar FIND ALL
        List<Organizacao> lista = dao.findAll();
        System.out.println("Total de organizações: " + lista.size());
        for (Organizacao o : lista) {
            System.out.println(o.getCodigo() + " - " + o.getNome());
        }

        // 3. Testar FIND BY ID (usa um código que você viu na listagem acima)
        Organizacao encontrada = dao.findByCodigo(1);
        System.out.println("Encontrada: " + encontrada.getNome());

        // 4. Testar UPDATE
        encontrada.setNome("Empresa Teste Atualizada");
        dao.update(encontrada);
        System.out.println("Atualizado com sucesso!");

        // 5. Testar DELETE (cuidado, isso apaga de verdade)
        // dao.delete(1);
    }
}