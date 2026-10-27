poly	START	0

.koda
first	LDT	x
	LDS	a0

	LDA	a1
	MULR	T, A
	ADDR	A, S

	LDA	a2
	MULR	T, A
	MULR	T, A
	ADDR	A, S

	LDA	a3
	MULR	T, A
	MULR	T, A
	MULR	T, A
	ADDR	A, S

	LDA	a4
	MULR	T, A
	MULR	T, A
	MULR	T, A
	MULR	T, A
	ADDR	A, S

	STS	res

halt	J	halt

. podatki
x	WORD	2
a0	WORD	5
a1	WORD	4
a2	WORD	3
a3	WORD	2
a4	WORD	1
res	RESW	1

	END	first