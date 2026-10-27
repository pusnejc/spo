arith	START	0

. koda
first	LDA	x
	ADD	y
	STA	sum

	LDA	x
	SUB	y
	STA	diff

	LDA	x
	MUL	y
	STA	prod

	LDA	x
	DIV	y
	STA	quot

. x % y = x - (x / y) * y
. x / y is already in A 
	MUL	y
	STA	temp
	LDA	x
	SUB	temp
	STA	mod	

halt 	J	halt

. podatki
x	WORD	15
y	WORD	6
temp	RESW	1
sum	RESW	1
diff	RESW	1
prod	RESW	1
quot	RESW	1
mod	RESW	1

	END	first