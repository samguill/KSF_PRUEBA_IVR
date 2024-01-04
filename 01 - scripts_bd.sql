CREATE USER IVR_CONSULTA IDENTIFIED BY password;

GRANT CREATE TABLE TO IVR_CONSULTA;

GRANT CREATE SESSION TO IVR_CONSULTA;

SELECT
    USERNAME,
    ACCOUNT_STATUS
FROM
    DBA_USERS
WHERE
    USERNAME = 'IVR_CONSULTA';

SQLPLUS IVR_CONSULTA/PASSWORD@//LOCALHOST:1521/OTECQ1

CREATE TABLE AREAS (
    CODE VARCHAR2(5),
    AREA_NAME VARCHAR2(50) PRIMARY KEY,
    BUDGET NUMBER
);

CREATE TABLE EMPLOYEES (
    DOCUMENT VARCHAR2(20) PRIMARY KEY,
    FIRST_NAME VARCHAR2(50),
    LAST_NAME VARCHAR2(50) NOT NULL,
    POSITION VARCHAR2(100),
    AREA VARCHAR2(50),
    ADMISSION_DATE DATE,
    CONSTRAINT FK_AREA FOREIGN KEY (AREA) REFERENCES AREAS(AREA_NAME)
);

ALTER USER IVR_CONSULTA QUOTA UNLIMITED ON USERS;

TRUNCATE TABLE IVR_CONSULTA.AREAS;

TRUNCATE TABLE IVR_CONSULTA.EMPLOYEES;

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00001',
    'IVR',
    1000000
);

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00010',
    'IVR_BANCO',
    100000
);

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00011',
    'KCRM',
    100000
);

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00012',
    'BPM',
    200000
);

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00020',
    'MARKETING',
    47000
);

INSERT INTO IVR_CONSULTA.AREAS VALUES(
    '00021',
    'RRHH',
    69000
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223344',
    'Alex',
    'Samaniego',
    'Jefe',
    'BPM',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223355',
    'Juan',
    'Chu',
    'Jefe',
    'IVR',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223356',
    'Pedro',
    'Chu',
    'Analista',
    'IVR',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223357',
    'Martha',
    'Nunez',
    'Lider',
    'IVR_BANCO',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223358',
    'Liz',
    'Barbachan',
    'Analista',
    'KCRM',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223359',
    'Gaby',
    'Perez',
    'Analista',
    'MARKETING',
    SYSDATE
);

INSERT INTO IVR_CONSULTA.EMPLOYEES VALUES(
    '11223360',
    'Gabriel',
    'Condori',
    'Analista',
    'RRHH',
    SYSDATE
);

SELECT
    *
FROM
    IVR_CONSULTA.AREAS;

SELECT
    *
FROM
    IVR_CONSULTA.EMPLOYEES;

-- a) Una lista con todos los empleados, en una sola columna llamada EQUIPOS que contenga la
--    siguiente estructura: nombre apellido (cargo – área) y esté ordenado alfabéticamente por área.
--    Ejemplo: Pedro Pérez (Analista – IVR)

SELECT
    E.FIRST_NAME
    || ' '
    || E.LAST_NAME
    || ' ('
    || E.POSITION
    || ' - '
    || A2.AREA_NAME
    || ')' AS EQUIPOS
FROM
    EMPLOYEES E
    INNER JOIN AREAS A2
    ON A2.AREA_NAME = E.AREA
ORDER BY
    A2.AREA_NAME;

-- b) Todos los cargos que existen, sin repeticiones

SELECT
    DISTINCT POSITION
FROM
    EMPLOYEES;

-- c) La cantidad de personas que hay en el área de IVR cuyo cargo es Analista.

SELECT
    COUNT(*) AS N_PERSONAS
FROM
    AREAS     A
    INNER JOIN EMPLOYEES E
    ON A.AREA_NAME = E.AREA
WHERE
    A.AREA_NAME = 'IVR'
    AND E.POSITION = 'Analista';

-- d) Obtener el presupuesto total de todas las áreas.

SELECT
    A.AREA_NAME,
    A.BUDGET
FROM
    AREAS A;

-- e) La cantidad de empleados que hay en cada área, ordenada de manera descendente.

SELECT
    E.AREA,
    COUNT(*) AS N_EMPLEADOS
FROM
    AREAS     A
    INNER JOIN EMPLOYEES E
    ON A.AREA_NAME = E.AREA
GROUP BY
    E.AREA
ORDER BY
    E.AREA DESC;

-- f) Obtener los documentos y nombres completos de los empleados que trabajen en áreas cuyo
--    presupuesto se encuentre entre 46000 y 70000 USD.

SELECT
    E.DOCUMENT,
    E.FIRST_NAME
    || ' '
    || E.LAST_NAME,
    E.AREA
FROM
    AREAS     A
    INNER JOIN EMPLOYEES E
    ON A.AREA_NAME = E.AREA
WHERE
    A.BUDGET BETWEEN 46000 AND 70000;

-- Debido a una reestructuración de la compañía se deben hacer los siguientes cambios:
-- g) Cambie los empleados del área IVR BANCO al área de IVR.

UPDATE EMPLOYEES
SET
    AREA = 'IVR'
WHERE
    AREA = 'IVR_BANCO';

COMMIT;

-- debe salir resultado vacio
SELECT
    *
FROM
    IVR_CONSULTA.EMPLOYEES
WHERE
    EMPLOYEES.AREA = 'IVR_BANCO';

-- aqui si deben de retornar todos los empleados del area de IVR
SELECT
    *
FROM
    IVR_CONSULTA.EMPLOYEES
WHERE
    EMPLOYEES.AREA = 'IVR';

-- h) Elimine los empleados que lleven 1 año o menos en la compañía.

DELETE FROM EMPLOYEES
WHERE
    EMPLOYEES.ADMISSION_DATE >= (SYSDATE - INTERVAL '1' YEAR);

COMMIT;

-- debe salir resultado vacio, todos los empleados fueron creados hoy
SELECT
    *
FROM
    IVR_CONSULTA.EMPLOYEES;