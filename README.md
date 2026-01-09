# d9k-LDF (Lazy Data Format)

Project state: just planning, no code yet.
Version: 0.0.1.

Feeling too lazy to implement [Event-based API | issue #5 | NNJSON](https://github.com/shinovon/NNJSON/issues/5) and decided to invent my own human-readable data format with simplest possible parser.

Data example:

```
__APP_VERSION__=0.1.3
__LDF_VERSION__=0.0.1
__BEGIN__
answer=42
users:array=
-0=
--name=Vovka
--year=2012
--height=1.7
--bio=Likes ski.\nLives in Novosibirsk.
--sosal=false
more examples=
-key with\==value with \=
-key with\:=value with \:
-\-1key=mind\-blowing
str1:string=0xab123
str2:string=false
__END__
```

## Format scheme

`[-][-]...[-]key[:data_type]=value`

## Data type

`[:data_type]` may be
- `:string` (to differ `"true"` from `true`, `"0x1F"` from `0x1F`).
- `:array` to differ array from object with numeric keys

Newlines in string values must be replaced with `\n`

## Escape

`=`, `:`, `-`, newlines  must be escaped: `\=`, `\:`, `\-`, `\n`, `\r`. `\n` can be serialized as `\\n`

## Numeric values

Valid numeric values: `12345`, `0x1F`, `0b1100`, `.563`.

## Special keys

Serialized data may or may not start with `__APP_VERSION__` key with string value.
Serialized data may or may not have `__BEGIN__` and `__END__` keys. If `__BEGIN__` is presented than lacking of the `__END__` will count as data transfer error.
There can be multiple `__BEGIN__`...`__END__` data records (for example for log file purposes)