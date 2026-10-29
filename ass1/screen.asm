scr	START	0

. A je argumentni register, po klicu rutine je lahko popackan
. ostali registri se ne popackajo

main	LDA	#97
	JSUB	scrfill

	JSUB	scrclear

	LDA	#98
	JSUB	scrfill

halt	J	halt


scrclear
	LDA	#0
scrfill
	STS	ssave
	STT	tsave

	RMO	A, S
	LDA	screen
	STA	scrptr

	ADD	scrlen
	RMO	A, T

. S holds character to print
. T holds end of screen
loop	RMO	S, A
	STCH	@scrptr

	LDA	scrptr
	ADD	#1
	STA	scrptr
	COMPR	A, T
	JLT	loop

	LDS	ssave
	LDT	tsave
	RSUB



. podatki
ssave	RESW	1
tsave	RESW	1
scrptr	RESW	1
screen	WORD	X'00B800'
scrcols	WORD	80
scrrows	WORD	25
scrlen	WORD	2000

	END	main