-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-10-2026 a las 07:54:21
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `veterinaria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categoria_producto`
--

CREATE TABLE `categoria_producto` (
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cita`
--

CREATE TABLE `cita` (
  `id_cita` int(11) NOT NULL,
  `numero_cita` int(11) NOT NULL,
  `id_mascota` int(11) NOT NULL,
  `id_veterinario` int(11) NOT NULL,
  `fecha_cita` date NOT NULL,
  `hora_cita` time NOT NULL,
  `estado` enum('Pendiente','Confirmada','Atendida','Cancelada') NOT NULL DEFAULT 'Pendiente'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `consulta`
--

CREATE TABLE `consulta` (
  `id_consulta` int(11) NOT NULL,
  `numero_consulta` int(11) NOT NULL,
  `id_cita` int(11) NOT NULL,
  `fecha_consulta` date NOT NULL,
  `estado_consulta` enum('En proceso','Finalizada','Cancelada') NOT NULL DEFAULT 'En proceso'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_consulta`
--

CREATE TABLE `detalle_consulta` (
  `id_detalle_consulta` int(11) NOT NULL,
  `id_consulta` int(11) NOT NULL,
  `numero_detalle` int(11) NOT NULL,
  `motivo_detalle` text NOT NULL,
  `tratamiento` text DEFAULT NULL,
  `dosis` varchar(100) DEFAULT NULL,
  `tiempo_dosis` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

CREATE TABLE `detalle_venta` (
  `id_detalle` int(11) NOT NULL,
  `id_venta` int(11) NOT NULL,
  `id_producto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(12,2) GENERATED ALWAYS AS (`cantidad` * `precio_unitario`) STORED
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `detalle_venta`
--
DELIMITER $$
CREATE TRIGGER `validar_detalle_venta_insert` BEFORE INSERT ON `detalle_venta` FOR EACH ROW BEGIN
    IF NEW.cantidad IS NULL OR NEW.cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor que cero';
    END IF;

    IF NEW.precio_unitario IS NULL OR NEW.precio_unitario < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio unitario debe ser cero o mayor';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `validar_detalle_venta_update` BEFORE UPDATE ON `detalle_venta` FOR EACH ROW BEGIN
    IF NEW.cantidad IS NULL OR NEW.cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La cantidad debe ser mayor que cero';
    END IF;

    IF NEW.precio_unitario IS NULL OR NEW.precio_unitario < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio unitario debe ser cero o mayor';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dueno`
--

CREATE TABLE `dueno` (
  `id_dueno` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `telefono` varchar(25) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mascota`
--

CREATE TABLE `mascota` (
  `id_mascota` int(11) NOT NULL,
  `id_dueno` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `especie` varchar(60) NOT NULL,
  `raza` varchar(100) DEFAULT NULL,
  `sexo` enum('Macho','Hembra','Desconocido') NOT NULL DEFAULT 'Desconocido',
  `fecha_nacimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_servicio`
--

CREATE TABLE `producto_servicio` (
  `id_producto` int(11) NOT NULL,
  `id_categoria` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `tipo` enum('Producto','Servicio') NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `stock` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `producto_servicio`
--
DELIMITER $$
CREATE TRIGGER `validar_producto_insert` BEFORE INSERT ON `producto_servicio` FOR EACH ROW BEGIN
    IF NEW.precio IS NULL OR NEW.precio < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio debe ser cero o mayor';
    END IF;

    IF NEW.tipo IS NULL
       OR NEW.tipo NOT IN ('Producto', 'Servicio') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El tipo debe ser Producto o Servicio';
    END IF;

    IF NEW.tipo = 'Producto' THEN
        IF NEW.stock IS NULL OR NEW.stock < 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Un producto debe tener stock de cero o mayor';
        END IF;
    ELSEIF NEW.tipo = 'Servicio' THEN
        IF NEW.stock IS NOT NULL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Un servicio debe tener stock NULL';
        END IF;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `validar_producto_update` BEFORE UPDATE ON `producto_servicio` FOR EACH ROW BEGIN
    IF NEW.precio IS NULL OR NEW.precio < 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El precio debe ser cero o mayor';
    END IF;

    IF NEW.tipo IS NULL
       OR NEW.tipo NOT IN ('Producto', 'Servicio') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El tipo debe ser Producto o Servicio';
    END IF;

    IF NEW.tipo = 'Producto' THEN
        IF NEW.stock IS NULL OR NEW.stock < 0 THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT =
                'Un producto debe tener stock de cero o mayor';
        END IF;
    ELSEIF NEW.tipo = 'Servicio' THEN
        IF NEW.stock IS NOT NULL THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Un servicio debe tener stock NULL';
        END IF;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `id_venta` int(11) NOT NULL,
  `id_dueno` int(11) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `veterinario`
--

CREATE TABLE `veterinario` (
  `id_veterinario` int(11) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `telefono` varchar(25) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_total_venta`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_total_venta` (
`id_venta` int(11)
,`id_dueno` int(11)
,`fecha` datetime
,`total` decimal(34,2)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_total_venta`
--
DROP TABLE IF EXISTS `vista_total_venta`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_total_venta`  AS SELECT `v`.`id_venta` AS `id_venta`, `v`.`id_dueno` AS `id_dueno`, `v`.`fecha` AS `fecha`, coalesce(sum(`d`.`subtotal`),0.00) AS `total` FROM (`venta` `v` left join `detalle_venta` `d` on(`d`.`id_venta` = `v`.`id_venta`)) GROUP BY `v`.`id_venta`, `v`.`id_dueno`, `v`.`fecha` ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categoria_producto`
--
ALTER TABLE `categoria_producto`
  ADD PRIMARY KEY (`id_categoria`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- Indices de la tabla `cita`
--
ALTER TABLE `cita`
  ADD PRIMARY KEY (`id_cita`),
  ADD UNIQUE KEY `numero_cita` (`numero_cita`),
  ADD KEY `id_mascota` (`id_mascota`),
  ADD KEY `id_veterinario` (`id_veterinario`);

--
-- Indices de la tabla `consulta`
--
ALTER TABLE `consulta`
  ADD PRIMARY KEY (`id_consulta`),
  ADD UNIQUE KEY `numero_consulta` (`numero_consulta`),
  ADD UNIQUE KEY `id_cita` (`id_cita`);

--
-- Indices de la tabla `detalle_consulta`
--
ALTER TABLE `detalle_consulta`
  ADD PRIMARY KEY (`id_detalle_consulta`),
  ADD UNIQUE KEY `id_consulta` (`id_consulta`,`numero_detalle`);

--
-- Indices de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `id_venta` (`id_venta`),
  ADD KEY `id_producto` (`id_producto`);

--
-- Indices de la tabla `dueno`
--
ALTER TABLE `dueno`
  ADD PRIMARY KEY (`id_dueno`);

--
-- Indices de la tabla `mascota`
--
ALTER TABLE `mascota`
  ADD PRIMARY KEY (`id_mascota`),
  ADD KEY `id_dueno` (`id_dueno`);

--
-- Indices de la tabla `producto_servicio`
--
ALTER TABLE `producto_servicio`
  ADD PRIMARY KEY (`id_producto`),
  ADD KEY `id_categoria` (`id_categoria`);

--
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`id_venta`),
  ADD KEY `id_dueno` (`id_dueno`);

--
-- Indices de la tabla `veterinario`
--
ALTER TABLE `veterinario`
  ADD PRIMARY KEY (`id_veterinario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categoria_producto`
--
ALTER TABLE `categoria_producto`
  MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cita`
--
ALTER TABLE `cita`
  MODIFY `id_cita` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `consulta`
--
ALTER TABLE `consulta`
  MODIFY `id_consulta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_consulta`
--
ALTER TABLE `detalle_consulta`
  MODIFY `id_detalle_consulta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  MODIFY `id_detalle` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `dueno`
--
ALTER TABLE `dueno`
  MODIFY `id_dueno` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `mascota`
--
ALTER TABLE `mascota`
  MODIFY `id_mascota` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto_servicio`
--
ALTER TABLE `producto_servicio`
  MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `venta`
--
ALTER TABLE `venta`
  MODIFY `id_venta` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `veterinario`
--
ALTER TABLE `veterinario`
  MODIFY `id_veterinario` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cita`
--
ALTER TABLE `cita`
  ADD CONSTRAINT `cita_ibfk_1` FOREIGN KEY (`id_mascota`) REFERENCES `mascota` (`id_mascota`),
  ADD CONSTRAINT `cita_ibfk_2` FOREIGN KEY (`id_veterinario`) REFERENCES `veterinario` (`id_veterinario`);

--
-- Filtros para la tabla `consulta`
--
ALTER TABLE `consulta`
  ADD CONSTRAINT `consulta_ibfk_1` FOREIGN KEY (`id_cita`) REFERENCES `cita` (`id_cita`);

--
-- Filtros para la tabla `detalle_consulta`
--
ALTER TABLE `detalle_consulta`
  ADD CONSTRAINT `detalle_consulta_ibfk_1` FOREIGN KEY (`id_consulta`) REFERENCES `consulta` (`id_consulta`);

--
-- Filtros para la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD CONSTRAINT `detalle_venta_ibfk_1` FOREIGN KEY (`id_venta`) REFERENCES `venta` (`id_venta`),
  ADD CONSTRAINT `detalle_venta_ibfk_2` FOREIGN KEY (`id_producto`) REFERENCES `producto_servicio` (`id_producto`);

--
-- Filtros para la tabla `mascota`
--
ALTER TABLE `mascota`
  ADD CONSTRAINT `mascota_ibfk_1` FOREIGN KEY (`id_dueno`) REFERENCES `dueno` (`id_dueno`);

--
-- Filtros para la tabla `producto_servicio`
--
ALTER TABLE `producto_servicio`
  ADD CONSTRAINT `producto_servicio_ibfk_1` FOREIGN KEY (`id_categoria`) REFERENCES `categoria_producto` (`id_categoria`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`id_dueno`) REFERENCES `dueno` (`id_dueno`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
