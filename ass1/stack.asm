stk	START	0

main	JSUB	stackinit

	LDA	#10
	STA	@stackptr
	JSUB	stackpush

	JSUB	stackpop
	LDA	@stackptr

halt	J	halt

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


. stack
stacksavea
	RESW	1
stackptr
	RESW	1
stack	RESW	1000

	END	main