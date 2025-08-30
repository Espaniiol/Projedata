package com.iniflex;

import com.iniflex.model.Funcionario;
import com.iniflex.util.FormatUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static List<Funcionario> funcionarios = new ArrayList<>();
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {

        try {
            inserirFuncionarios();
            System.out.println("✅ 3.1 - Funcionários inseridos com sucesso!\n");

            removerFuncionario("João");
            System.out.println("✅ 3.2 - Funcionário João removido da lista!\n");

            imprimirTodosFuncionarios();

            aplicarAumento();
            System.out.println("✅ 3.4 - Aumento de 10% aplicado nos salários!\n");

            Map<String, List<Funcionario>> funcionariosPorFuncao = agruparPorFuncao();
            imprimirFuncionariosPorFuncao(funcionariosPorFuncao);

            imprimirAniversariantes();

            imprimirFuncionarioMaisVelho();

            imprimirFuncionariosOrdemAlfabetica();

            imprimirTotalSalarios();

            imprimirSalariosMinimos();


        } catch (Exception e) {
            System.err.println("❌ Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inserirFuncionarios() {
        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));
    }

    private static void removerFuncionario(String nome) {
        boolean removido = funcionarios.removeIf(funcionario -> funcionario.getNome().equals(nome));
        if (!removido) {
            System.out.println("⚠️ Funcionário " + nome + " não encontrado!");
        }
    }

    private static void imprimirTodosFuncionarios() {
        System.out.println("📋 3.3 - LISTA DE TODOS OS FUNCIONÁRIOS:");
        System.out.println("═".repeat(90));

        funcionarios.forEach(funcionario -> {
            System.out.printf("👤 %-10s │ 📅 %s │ 💰 R$ %15s │ 🏢 %-15s%n",
                    funcionario.getNome(),
                    FormatUtils.formatDate(funcionario.getDataNascimento()),
                    FormatUtils.formatCurrency(funcionario.getSalario()),
                    funcionario.getFuncao());
        });
        System.out.println("═".repeat(90) + "\n");
    }

    private static void aplicarAumento() {
        funcionarios.forEach(funcionario -> {
            BigDecimal novoSalario = funcionario.getSalario().multiply(new BigDecimal("1.10"));
            funcionario.setSalario(novoSalario.setScale(2, RoundingMode.HALF_UP));
        });
    }

    private static Map<String, List<Funcionario>> agruparPorFuncao() {
        return funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    private static void imprimirFuncionariosPorFuncao(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        System.out.println("🏢 3.6 - FUNCIONÁRIOS AGRUPADOS POR FUNÇÃO:");
        System.out.println("═".repeat(90));

        funcionariosPorFuncao.forEach((funcao, listaFuncionarios) -> {
            System.out.println("🔹 FUNÇÃO: " + funcao.toUpperCase());
            System.out.println("─".repeat(60));
            listaFuncionarios.forEach(funcionario -> {
                System.out.printf("   👤 %-10s │ 📅 %s │ 💰 R$ %15s%n",
                        funcionario.getNome(),
                        FormatUtils.formatDate(funcionario.getDataNascimento()),
                        FormatUtils.formatCurrency(funcionario.getSalario()));
            });
            System.out.println();
        });
    }

    private static void imprimirAniversariantes() {
        System.out.println("🎂 3.8 - ANIVERSARIANTES DE OUTUBRO E DEZEMBRO:");
        System.out.println("═".repeat(90));

        List<Funcionario> aniversariantes = funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .collect(Collectors.toList());

        if (aniversariantes.isEmpty()) {
            System.out.println("ℹ️ Nenhum funcionário faz aniversário em outubro ou dezembro.");
        } else {
            aniversariantes.forEach(funcionario -> {
                String mes = funcionario.getDataNascimento().getMonthValue() == 10 ? "Outubro" : "Dezembro";
                System.out.printf("🎉 %-10s │ 📅 %s │ 📅 %s%n",
                        funcionario.getNome(),
                        FormatUtils.formatDate(funcionario.getDataNascimento()),
                        mes);
            });
        }
        System.out.println();
    }

    private static void imprimirFuncionarioMaisVelho() {
        System.out.println("👴 3.9 - FUNCIONÁRIO COM MAIOR IDADE:");
        System.out.println("═".repeat(90));

        Optional<Funcionario> maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento));

        if (maisVelho.isPresent()) {
            Funcionario funcionario = maisVelho.get();
            int idade = Period.between(funcionario.getDataNascimento(), LocalDate.now()).getYears();
            System.out.printf("🏆 Nome: %s │ 🎂 Idade: %d anos%n", funcionario.getNome(), idade);
        } else {
            System.out.println("ℹ️ Nenhum funcionário encontrado.");
        }
        System.out.println();
    }

    private static void imprimirFuncionariosOrdemAlfabetica() {
        System.out.println("🔤 3.10 - FUNCIONÁRIOS EM ORDEM ALFABÉTICA:");
        System.out.println("═".repeat(90));

        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(funcionario -> {
                    System.out.printf("👤 %-10s │ 📅 %s │ 💰 R$ %15s │ 🏢 %-15s%n",
                            funcionario.getNome(),
                            FormatUtils.formatDate(funcionario.getDataNascimento()),
                            FormatUtils.formatCurrency(funcionario.getSalario()),
                            funcionario.getFuncao());
                });
        System.out.println();
    }

    private static void imprimirTotalSalarios() {
        System.out.println("💰 3.11 - TOTAL DOS SALÁRIOS:");
        System.out.println("═".repeat(90));

        BigDecimal total = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf("🎯 Total dos Salários: R$ %s%n", FormatUtils.formatCurrency(total));
        System.out.println();
    }

    private static void imprimirSalariosMinimos() {
        System.out.println("📊 3.12 - EQUIVALÊNCIA EM SALÁRIOS MÍNIMOS (R$ " + FormatUtils.formatCurrency(SALARIO_MINIMO) + "):");
        System.out.println("═".repeat(90));

        funcionarios.forEach(funcionario -> {
            BigDecimal quantidadeSalariosMinimos = funcionario.getSalario()
                    .divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);

            System.out.printf("👤 %-10s │ 💰 R$ %15s │ 📊 %s salários mínimos%n",
                    funcionario.getNome(),
                    FormatUtils.formatCurrency(funcionario.getSalario()),
                    FormatUtils.formatCurrency(quantidadeSalariosMinimos));
        });
        System.out.println();
    }
}