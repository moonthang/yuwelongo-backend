# YuweLongo - Backend Service

[![Estado del Proyecto](https://img.shields.io/badge/estado-en%20desarrollo-yellowgreen)](https://github.com/tuusuario/YuweLongo-Backend)
[![NetBeans](https://img.shields.io/badge/NetBeans-24-blue?logo=apache-netbeans-ide)](https://netbeans.apache.org/)
[![GlassFish](https://img.shields.io/badge/GlassFish-7.0.15-orange?logo=java)](https://projects.eclipse.org/projects/ee4j.glassfish)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-purple?logo=jakartaee)](https://jakarta.ee/)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Seguridad-important?logo=jsonwebtokens)](https://jwt.io/)
[![JPA / Hibernate](https://img.shields.io/badge/JPA%20%2F%20Hibernate-Persistencia-success?logo=hibernate)](https://hibernate.org/)

## Descripción del Proyecto
El backend de **YuweLongo** es una API RESTful, diseñada para gestionar la lógica y la persistencia de datos del sitio web YuweLongo, diccionario de la lengua indigena Nasa Yuwe el cual tmabien incluye un juego interactivo.  
Este servicio se enfoca en proporcionar endpoints eficientes para las operaciones CRUD y el control de acceso basado en roles.

---

## 📑 Tabla de Contenido

1. [⚔️ YuweLongo - Backend Service](#️-yuwelongo---backend-service)
2. [🎯 Descripción del Proyecto](#-descripción-del-proyecto)
3. [🚀 Funcionalidades Clave](#-funcionalidades-clave)
4. [🛠️ Stack Tecnológico](#️-stack-tecnológico)
5. [💾 Esquema de la Base de Datos](#-esquema-de-la-base-de-datos)
6. [⚙️ Despliegue y Configuración Local](#️-despliegue-y-configuración-local)
   1. [1️⃣ Prerrequisitos](#1️⃣-prerrequisitos)
   2. [2️⃣ Configuración de la Base de Datos](#2️⃣-configuración-de-la-base-de-datos)
      1. [A. Credenciales de la Base de Datos](#a-credenciales-de-la-base-de-datos)
      2. [B. Configuración del Servidor GlassFish (Crucial)](#b-configuración-del-servidor-glassfish-crucial)
         1. [1️⃣ Configuración del JDBC Connection Pool (jpaYuwelongo)](#1️⃣-configuración-del-jdbc-connection-pool-jpayuwelongo)
         2. [2️⃣ Configuración del JDBC Resource (jdbc__yuwe)](#2️⃣-configuración-del-jdbc-resource-jd__yuwe)
         3. [3️⃣ Compilación y Despliegue](#3️⃣-compilación-y-despliegue)

---

## 🚀 Funcionalidades Clave 
- **Gestión CRUD de Usuarios:** Servicio completo para la creación, lectura, actualización y eliminación de perfiles de usuario.  
- **Autenticación (Auth):** Manejo de inicio de sesión y validación de credenciales.
- **Cifrado de contraseñas (BCrypt):** Hash adaptativo de contraseñas mediante implementación de BCrypt.  
- **Autorización Segura (JWT):** Implementación de JSON Web Tokens (JWT) para proteger los endpoints y determinar los permisos del usuario (ADMIN vs. USUARIO).  
- **Gestión de Contenido:** Manejo de categorías, palabras y preguntas para la funcionalidad de juego/aprendizaje.

---

## 🛠️ Stack Tecnológico

| 🧩 Componente | 🚀 Tecnología | ⚙️ Versión / Uso |
|----------------|----------------|------------------|
| **IDE** | <img src="https://upload.wikimedia.org/wikipedia/commons/9/98/Apache_NetBeans_Logo.svg" width="20"/> NetBeans | NetBeans 24 |
| **Servidor de Aplicaciones** | <img src="https://upload.wikimedia.org/wikipedia/commons/1/1b/GlassFish_logo.svg" width="20"/> Eclipse GlassFish | GlassFish 7.0.15 |
| **Base de Datos** | <img src="https://upload.wikimedia.org/wikipedia/en/d/dd/MySQL_logo.svg" width="30"/> MySQL | Motor de persistencia |
| **Gestor DB** | 🧰 XAMPP | v3.3.0 *(proveedor de MySQL)* |
| **Framework** | <img src="https://upload.wikimedia.org/wikipedia/commons/4/4e/Jakarta_EE_logo.svg" width="20"/> Jakarta EE / Java Servlets | Construcción de la API REST |
| **Seguridad** | <img src="https://jwt.io/img/pic_logo.svg" width="20"/> JWT | Implementación de tokens de sesión |
| **Persistencia** | <img src="https://upload.wikimedia.org/wikipedia/commons/6/6d/Hibernate_logo.svg" width="20"/> JPA / Hibernate *(asumido)* | Mapeo Objeto-Relacional |

---

## 💾 Esquema de la Base de Datos

La base de datos está diseñada para soportar la gestión de usuarios, contenido (palabras y categorías) y la funcionalidad de juego/aprendizaje.

| Tabla | Propósito Principal | Clave Primaria | Relaciones Importantes |
|--------|----------------------|----------------|------------------------|
| **usuarios** | Almacena la información principal de los usuarios (incluye rol). | id_usuario | - |
| **categorias** | Organiza el contenido por temas o grupos. | id_categoria | palabras (1:N) |
| **niveles_juego** | Define los niveles de dificultad para la jugabilidad. | id_nivel | juego, preguntas_juego |
| **palabras** | Contenido principal (palabra, traducción, ejemplos, audio/imagen). | id_palabra | categorias (N:1), favoritos, preguntas_juego |
| **favoritos** | Relación N:M que guarda las palabras que cada usuario ha marcado como favoritas. | id_favorito | usuarios (N:1), palabras (N:1) |
| **juego** | Registra las sesiones de juego de los usuarios (puntaje, fecha, preguntas). | id_juego | usuarios, niveles_juego |
| **preguntas_juego** | Define las preguntas del juego, sus opciones de respuesta y valor XP. | id_pregunta | palabras, niveles_juego |
| **favorito** | Tabla de unión con misma función que favoritos. Se recomienda consolidar. | id_favorito | palabras, usuarios |

---

## ⚙️ Despliegue y Configuración Local

### 1️⃣ Prerrequisitos
Asegúrate de tener instalados y configurados:
- NetBeans 24 (o superior)
- JDK 17 (o versión compatible con GlassFish 7)
- GlassFish 7.0.15 configurado en NetBeans
- XAMPP v3.3.0 (o servidor MySQL equivalente) con el servicio MySQL activo

---

### 2️⃣ Configuración de la Base de Datos

#### A. Credenciales de la Base de Datos

| Parámetro | Valor |
|------------|--------|
| Nombre de la Base de Datos | `yuwelongo` |
| Usuario | `userYuwe` |
| Contraseña | `123456` |

**SQL de ejemplo para la creación de DB y credenciales (ejecutar en MySQL):**
-- Crea la base de datos si no existe
CREATE DATABASE IF NOT EXISTS yuwelongo;

-- Crea el usuario
CREATE USER 'userYuwe'@'localhost' IDENTIFIED BY '123456';

-- Otorga permisos al usuario
GRANT ALL PRIVILEGES ON yuwelongo.* TO 'userYuwe'@'localhost';
FLUSH PRIVILEGES; 

### B. Configuración del Servidor GlassFish (Crucial)

El proyecto utiliza un **Pool de Conexiones JDBC** y un **Recurso JDBC** para la gestión de la base de datos.  
Esta configuración debe realizarse manualmente a través de la **Consola de Administración de GlassFish**:  
👉 [http://localhost:4848](http://localhost:4848)

| Detalle | Configuración |
|----------|----------------|
| **Consola GlassFish** | http://localhost:4848 |
| **JNDI Name (Recurso)** | `jdbc/__yuwe` |
| **Target Connection Pool (Pool)** | `jpaYuwelongo` |

---

### 1️⃣ Configuración del JDBC Connection Pool (`jpaYuwelongo`)

En la consola de GlassFish, navega a  
**Resources → JDBC → JDBC Connection Pools**  
y crea uno nuevo con las siguientes propiedades:

| Propiedad | Valor |
|------------|--------|
| **Pool Name** | jpaYuwelongo |
| **Resource Type** | javax.sql.DataSource |
| **Driver Vendor** | MySql |
| **Database URL** | jdbc:mysql://localhost:3306/yuwelongo |
| **Datasource Classname** | com.mysql.cj.jdbc.MysqlDataSource |
| **User** | userYuwe |
| **Password** | 123456 |

---

### 2️⃣ Configuración del JDBC Resource (`jdbc/__yuwe`)

Navega a  
**Resources → JDBC → JDBC Resources**  
y crea un nuevo recurso vinculado al pool creado:

- **JNDI Name:** `jdbc/__yuwe`  
- **Pool Name:** `jpaYuwelongo`

---

### 3️⃣ Compilación y Despliegue

1. Asegúrate de que el **GlassFish Server 7.0.15** esté iniciado.  
2. En **NetBeans**, haz clic derecho sobre el proyecto **YuweLongo-Backend-1.0-SNAPSHOT**.  
3. Selecciona **Run (Ejecutar)** o **Deploy (Desplegar)**.  

> La URL base de tu API será similar a:  
> **http://localhost:8080/YuweLongo-Backend/api/**

---

## 👨‍💻 Autores

**YuweLongo - Backend** fue desarrollado por **Miguel Angel Sepulveda Burgos** y **Faeli Yobana Nez fiole**.

*   <img src="https://cdn.worldvectorlogo.com/logos/github-icon-2.svg" width="20" height="20"/> GitHub: [@moonthang](https://github.com/moonthang)
*   <img src="https://static.vecteezy.com/system/resources/previews/018/930/480/non_2x/linkedin-logo-linkedin-icon-transparent-free-png.png" width="20" height="20"/> LinkedIn: [Miguel Ángel Sepulveda Burgos](https://www.linkedin.com/in/miguel-%C3%A1ngel-sep%C3%BAlveda-burgos-a87808167/)

---

