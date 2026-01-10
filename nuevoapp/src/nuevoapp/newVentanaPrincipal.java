     * Muestra los botones de navegación que son comunes para todos los usuarios.
     */
    private void habilitarNavegacionBasica() {
        btnInicio.setVisible(true);
        btnEquipos.setVisible(true);
        btnJugadores.setVisible(true);
        btnPartidos.setVisible(true);
        btnClasificacin.setVisible(true);
    }

    /**
     * Activa todos los componentes de la interfaz (Exclusivo para Administrador).
     */
    private void mostrarTodo(boolean estado) {
        habilitarNavegacionBasica();
        
        // Paneles y botones de administración
        panelAdminPartidos.setVisible(estado);
        btnNuevaTemp.setVisible(estado);
        btnNuevaJor.setVisible(estado);
        btnNuevoPart.setVisible(estado);
        
        // Botones de equipos
        btnAgregarEquipo.setVisible(estado);
        btnInscribirEquipo.setVisible(estado);
        
        // Botones de jugadores
        btnAgregarJugador.setVisible(estado);
        btnCambiarEquipo.setVisible(estado);
       
        btnVerFoto.setVisible(estado);
    }}