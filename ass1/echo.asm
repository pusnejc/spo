echo	START	0

. Argument bo v registru A.
. Funkcija pricakuje, da bo po klicu neke subrutine A popackan.

main	LDA	#str
	JSUB	string

	JSUB	nl

	LDCH	chr
	JSUB	char

	JSUB	nl

	+LDA	#12345
	JSUB	num

	JSUB	nl

halt	J	halt

str	BYTE	C'Hello, SIC/XE!'
	BYTE	X'00'
chr	BYTE	C'A'


char	WD	#1
	RSUB


nl	LDCH	#10
	WD	#1
	RSUB


. A holds address of string
string	STA saddr

sloop	CLEAR	A
	LDCH	@saddr
	COMP	#0
	JEQ	endstr
	WD	#1
	LDA	saddr
	ADD	#1
	STA	saddr
	J	sloop

endstr	RSUB

saddr	RESW	1


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

. ni rekurzije, lahko imamo en skupen memory address za posamezen register
ssave	RESW	1
tsave	RESW	1

ascii0	WORD	48

bufadr	RESW	1
buf	RESB	10

	END main