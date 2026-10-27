horner	START	0

.koda
first	LDT	x

. x^4 + 2x^3 + 3x^2 + 4x + 5
. (((x + 2)x + 3)x + 4)x + 5

	LDA	a4
	RMO	T, A
	ADD	a3

	MULR	T, A
	ADD	a2

	MULR	T, A
	ADD	a1

	MULR	T, A
	ADD	a0

	STA	res

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