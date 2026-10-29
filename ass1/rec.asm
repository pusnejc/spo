rec	START	0

main	JSUB	stackinit

. build number (assuming it's a non-negative integer)
. read next character into A
. if it's \n, build factorial
oloop	CLEAR	S
	CLEAR	T

. S holds the number being built
iloop	CLEAR	A
	RD	#0xFA

. ignore CR (Windows)
        COMP    #13
        JEQ     iloop

. if \n, full number has been read
        COMP    #10
        JEQ     endilp

	LDT	#10
	MULR	T, S
	SUB	ascii0
	ADDR	A, S

	J iloop
endilp

. S has the whole number
. move it to A and calculate factorial
. then print it to stdout
	RMO	S, A

. if number = 0, stop
        COMP    #0
        JEQ     halt

	JSUB	factorial

	JSUB	num
	JSUB	nl

	J	oloop

halt	J	halt



. v registru A vrne rezultat
factorial
	STL	@stackptr
	JSUB	stackpush

	COMP	#1
	JEQ	endf

	STA	@stackptr
	JSUB	stackpush
	SUB	#1
	JSUB	factorial
	JSUB	stackpop
	MUL	@stackptr

endf	JSUB	stackpop
	LDL	@stackptr
	RSUB



. =============== newline printing ===============

nl	LDCH	#10
	WD	#1
	RSUB



. ========== integer printing to stdout ==========

num	STS	ssave
	STT	tsave
	COMP	#0
	JEQ	if
	J	else

if	LDA	ascii0
	WD	#1
	J	endnum

else	RMO	A, S
	LDA	#buf
	STA	bufadr
	RMO	S, A

. if negative number, print the -, then multiply by -1
	COMP	#0
	JGT	nloop
	RMO	A, S
	LDA	#45
	WD	#1
	RMO	S, A
	MUL	#-1

. number is in A
nloop	COMP	#0
	JEQ	print	

. digit = x % 10 = x - (x/10)*10
. digit -> digit in ascii
. digit -> buf
. bufadr++
. x = x / 10

	RMO	A, S
	DIV	#10
	MUL	#10
	RMO	A, T
	RMO	S, A
	SUBR	T, A

	ADD	ascii0

	STCH	@bufadr

	LDA	bufadr
	ADD	#1
	STA	bufadr

	RMO	S, A
	DIV	#10

	J nloop

. bufadr--
. if bufadr <= #buf then J end
. print digit
. back to print
print	LDA	bufadr
	SUB	#1
	
	COMP	#buf
	JLT	endnum
	STA	bufadr

	LDCH	@bufadr
	WD	#1

	J	print

endnum	LDS	ssave
	LDT	tsave
	RSUB

ssave	RESW	1
tsave	RESW	1
ascii0	WORD	48
bufadr	RESW	1
buf	RESB	10



. ==================== stack ====================

stackinit
	STA	stacksavea
	LDA	#stack
	STA	stackptr
	LDA	stacksavea
	RSUB

stackpush
	STA	stacksavea
	LDA	stackptr
	ADD	#3
	STA	stackptr
	LDA	stacksavea
	RSUB

stackpop
	STA	stacksavea
	LDA	stackptr
	SUB	#3
	STA	stackptr
	LDA	stacksavea
	RSUB

stacksavea
	RESW	1
stackptr
	RESW	1
stack	RESW	1000

	END	main