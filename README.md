# 🏥 Sistema de Gestión de Citas Médicas

## 📋 Descripción
Sistema web desarrollado con Spring Boot para gestionar citas médicas en el área de Medicina Interna. Permite a los doctores y personal administrativo gestionar citas, consultorios y horarios de manera eficiente.

## 🚀 Características Principales
- Gestión completa de citas médicas
- Asignación de consultorios
- Control de horarios de doctores
- Validaciones automáticas para evitar conflictos
- Interfaz intuitiva y responsiva

## ⚙️ Reglas de Negocio
- No se pueden agendar citas en el mismo consultorio a la misma hora
- No se pueden agendar citas para un mismo doctor a la misma hora
- Mínimo 2 horas entre citas para el mismo paciente
- Máximo 8 citas por día por doctor

## 🛠️ Tecnologías Utilizadas
- Java 17
- Spring Boot 3.4.0
- MySQL
- Thymeleaf
- Bootstrap 5
- Gradle

## 🔧 Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/tu-usuario/tu-repositorio.git