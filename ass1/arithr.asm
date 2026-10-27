arithr	START	0

. koda
first	LDS	x
	LDT	y

	RMO	S, A
	ADDR	T, A
	STA	sum

	RMO	S, A
	SUBR	T, A
	STA	diff

	RMO	S, A
	MULR	T, A
	STA	prod

	RMO	S, A
	DIVR	T, A
	STA	quot

. x % y = x - (x / y) * y
. x / y is already in A 
	MULR	T, A
	RMO	A, T
	RMO	S, A
	SUBR	T, A
	STA	mod

halt	J	halt

. podatki
x	WORD	15
y	WORD	6
sum	RESW	1
diff	RESW	1
prod	RESW	1
quot	RESW	1
mod	RESW	1

	END first