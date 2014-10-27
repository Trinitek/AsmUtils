	org 0x100

start:
	mov	ax, 0x13
	int	0x10

setPalette:
	mov	dx, 0x03C8
	mov	al, 0
	out	dx, al
	inc	dx
	mov	cx, image-palette
	mov	si, palette

	.writeRGB:
	lodsb
	shr	al, 2
	out	dx, al
	loop	.writeRGB

drawImage:
	mov	ax, 0xA000
	mov	es, ax
	xor	di, di
	mov	cx, 64000	; 320*200
	mov	si, image

	rep	movsb

exit:
	xor	ax, ax
	int	0x16
	mov	ax, 0x03
	int	0x10
	ret

palette:
	file 'image.pal'

image:
	file 'image.pxl'