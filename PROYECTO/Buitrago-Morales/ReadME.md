Analisis antes de arreglar el couverage:

analisis-DOPO - Reporte de Cobertura
=====================================

+-------------+---------------------+------+-----------------+------+--------+------+--------+-------+--------+---------+--------+---------+
| Element     | Missed Instructions | Cov. | Missed Branches | Cov. | Missed | Cxty | Missed | Lines | Missed | Methods | Missed | Classes |
+-------------+---------------------+------+-----------------+------+--------+------+--------+-------+--------+---------+--------+---------+
| tower       |                     |  58% |                 |  50% |    185 |  328 |    334 |   765 |     46 |     120 |      6 |      14 |
| shapes      |                     |  56% |                 |  45% |     39 |   71 |     79 |   190 |     26 |      50 |      2 |       7 |
| org.example |                     |   0% |                 |  n/a |      2 |    2 |      3 |     3 |      2 |       2 |      1 |       1 |
+-------------+---------------------+------+-----------------+------+--------+------+--------+-------+--------+---------+--------+---------+
| Total       |      1,717 of 4,065 |  57% |      224 of 452 |  50% |    226 |  401 |    416 |   958 |     74 |     172 |      9 |      22 |
+-------------+---------------------+------+-----------------+------+--------+------+--------+-------+--------+---------+--------+---------+

Cobertura general: 57% de instrucciones y 50% de ramas — está por debajo del estándar recomendado (80%).
Por paquete:

tower es el más crítico: tiene 185 instrucciones sin cubrir de 328 (58%) y 334 líneas sin cubrir de 765. Es el paquete más grande y el que más atención necesita.
shapes tiene cobertura similar (56%), con 39 instrucciones y 79 líneas sin cubrir. Está en un estado parecido a tower.
org.example es el más preocupante proporcionalmente: 0% de cobertura, aunque es pequeño (solo 2 métodos, 3 líneas). Probablemente es código de configuración o arranque sin pruebas.

Puntos clave:

De 172 métodos totales, 74 no tienen ninguna prueba (43%).
De 22 clases totales, 9 no están cubiertas (41%).
La cobertura de ramas al 50% indica que muchos condicionales (if/else, switch) no están siendo ejercitados por los tests.

