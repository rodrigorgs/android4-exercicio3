# Exercício 3

O exercício consiste no desenvolvimento de um aplicativo para gerenciar uma lista de tarefas. Cada tarefa possui uma descrição e uma prioridade entre 1 e 10, de forma que, quanto menor o número, mais prioritária é a tarefa. Não pode haver duas tarefas com a mesma descrição. A lista de tarefas é ordenada por prioridade, da mais prioritária para a menos prioritária; no caso de tarefas com prioridade igual, usa-se a ordem de inserção (tarefas adicionadas mais recentemente aparecem no final).

Note que o layout já está pronto, assim como a classe `Tarefa`. As tarefas devem ser exibidas em uma `ListView` cujas células seguem o layout `android.R.layout.simple_list_item_2`.

A interface gráfica está ilustrada na imagem `screenshot.png` nesta pasta. O botão `Adicionar` insere uma tarefa na lista, com os dados digitados pelo usuário (se os dados forem válidos). O botão `Remover 1º` remove o primeiro item da lista. Sempre que a lista estiver vazia, o botão deve estar desativado. Clicar em um item também resulta na sua remoção.

Mensagens:

- Ao tentar inserir uma tarefa com a mesma descrição de uma tarefa da lista, o aplicativo deve exibir, no `TextView` `tvStatus`, o texto "Tarefa já cadastrada."
- Ao tentar inserir uma tarefa com prioridade inválida, o aplicativo deve exibir, no `TextView` `tvStatus`, o texto "A prioridade deve estar entre 1 e 10.".
- Ao inserir ou remover algum elemento com sucesso, o texto do `TextView` `tvStatus` deve ser limpo.

## Dicas

Dê uma olhada em métodos da classe `ArrayAdapter`, como `add`, `remove` e `notifyDataSetChanged`.

Você também pode querer usar o método estático `Collections.sort` para garantir a ordem das tarefas.
