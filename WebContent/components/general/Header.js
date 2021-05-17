const template = /*html*/ `
  <header class="m-header">
    <img src="../../morebread/static/imgs/logo.svg" />
    <h2>Morebread</h2>

    <div class="header-items">
      <RouterLink to="/usuarios">Usuarios</RouterLink>
      <RouterLink to="/produtos">Produtos</RouterLink>
      <RouterLink to="/vendas">Vendas</RouterLink>
      <RouterLink to="/relatorio">Relatório</RouterLink>
    </div>
  </header>
`;

export default {
  template
}