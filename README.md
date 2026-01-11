# d9k-LDF (Lazy Data Format)

Project state: just planning, no code yet.
Version: 0.0.4.

## Reason

Feeling too lazy to implement [Event-based API | issue #5 | NNJSON](https://github.com/shinovon/NNJSON/issues/5) and decided to invent my own human-readable data format with simplest possible parser.

## Serialized data example

```
__LDF_VERSION__=0.0.4
__APP_VERSION__=0.1.3
__BEGIN__=app data
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

`=`, `:`, `-`, newlines, tabs must be escaped: `\=`, `\:`, `\-`, `\n`, `\r`, `\t`.

`\n` can be serialized as `\\n`

TODO: Must spaces at the end of text string be escaped: `My text string with spaces at the end...\ \ \ \ `?

## Numeric values

Valid numeric values examples: `12345`, `0x1F`, `0b1100`, `.563`, `0.33`.

## Special keys

Serialized data may or may not start with `__APP_VERSION__` key with string value.
Serialized data may or may not have `__BEGIN__` and `__END__` keys. If `__BEGIN__` is presented than lacking of the `__END__` will count as data transfer error.
There can be multiple `__BEGIN__`...`__END__` data records (for example for log file purposes).
`__BEGIN__` key can have string label value, for example `__BEGIN__=Debug data`.

Errors: `ErrorNestedDataRecord`

## Deserializer events methods

(Not implemented yet)

```
onArrayBegin(key)
onArrayEnd()
onObjectBegin(key)
onObjectEnd()
onString(key, value)
onNumber(key, value)
onBoolean(key, value)
```

— these methods can be overriden in `LdfDeserializer` inherited classes.

Errors: `ErrorNonIntegerKeyInArray`, `NegativeKeyInArray`.

## Serializer methods

(Not implemented yet)

```
arrayBegin(key)
arrayEnd()
objectBegin(key)
objectEnd()
stringField(key, value)
numberField(key, value)
booleanField(key, value)
```

Top level may be only object. You're always already inside the object when you start serializing.

### Serializing single values

Single value can be represented with special `__VALUE__` key:

`__VALUE__:string=true`

Errors: `ErrorSingleValueExpected` if other fields present.

## User-extensibility: types

User can define custom data types to interpret them manually:

```
h:OrderedHashtable=
-0=
--key:a
--value:1
```