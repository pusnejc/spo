print	START	0

.koda
first	LDA	#text
	STA	addr
		
. SIC/XE = 6 chars

loop	LDCH	@addr
	WD	dev
	LDA	addr
	ADD	#1
	STA	addr
	COMP	#dev
	JLT	loop

halt	J	halt

.podatki
addr	RESW	1
text	BYTE	C'SIC/XE'
nl	BYTE	X'0A'
dev	BYTE	X'AA'
	END	first