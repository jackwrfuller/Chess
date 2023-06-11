# Jack's Chess Game
A first attempt at creating chess application.

## Game rules implemented:
- [x] Move legality checking:
    - [x] Pawns
    - [x] En passant
    - [x] Kings (without checking for checks)
    - [x] Kings (with checking for check)
    - [x] Rooks
    - [x] Bishops
    - [x] Queens
    - [x] Knights
    - [x] Castling
- [ ] Pawn promotion
- [x] Checking
- [ ] Checkmate
- [ ] Stalemate
- [ ] 50 move rule
- [ ] Draw by repetition
- [ ] Universal Chess Interface (UCI) compatability

## Known issues
- The game stores the location of an en passant target pawn, rather than the square in which will get
moved into by the enemy. I.e if white's a-file pawn moves to a4, we need to store the en passant
target as "a3" as this aligns with FEN notation.
 
## Code to be reviewed

This is for sections of code that I'm not happy with, and think needs reviewing:

- [ ] OH GOD this is all spaghetti code. Just reading "Clean Code" atm and it has opened
my eyes goddamn
- [ ] En passant functionality. It seems overly complicated and involves too much repetition.
    I reckon it could be optimised and refactored significantly.