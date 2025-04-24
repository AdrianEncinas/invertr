import React from 'react';

const HomePage: React.FC = () => {
  return (
    <div className="container">
      <h1>Bienvenido a Invertrack</h1>
      <p>
        Tu plataforma para el seguimiento y gestión de inversiones.
        Mantén un registro detallado de tu portafolio y toma decisiones informadas.
      </p>
      <div className="features">
        <div className="feature">
          <h2>Seguimiento en Tiempo Real</h2>
          <p>Monitorea tus inversiones con actualizaciones en tiempo real</p>
        </div>
        <div className="feature">
          <h2>Análisis Detallado</h2>
          <p>Obtén análisis detallados de tu rendimiento y diversificación</p>
        </div>
        <div className="feature">
          <h2>Gestión Simplificada</h2>
          <p>Administra tu portafolio de manera sencilla y eficiente</p>
        </div>
      </div>
    </div>
  );
};

export default HomePage; 