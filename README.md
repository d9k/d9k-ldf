# d9k-LDF (Lazy Data Format)

Project state: just planning, no code yet.
Version: 0.0.5.

## Reason

Feeling too lazy to implement [Event-based API | issue #5 | NNJSON](https://github.com/shinovon/NNJSON/issues/5) and decided to invent my own human-readable data format with simplest possible parser.

## Serialized data example

```
LDF_VERSION=0.0.5
LDF_APP_VERSION=0.1.3
LDF_BEGIN=app data
LDF_COMMENT=special comment
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
LDF_END
```

## Format scheme

`[-][-]...[-]key[:data_type]=value`

Errors: `ErrorUnexpectedNestedLevelIncrease`

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

Special keys begin with `LDF_`. Data structures can't have fields names beginning with `LDF_`.

Serialized data may or may not start with `LDF_APP_VERSION` key with string value.
Serialized data may or may not have `LDF_BEGIN` and `LDF_END` keys. If `LDF_BEGIN` is presented than lacking of the `LDF_END` will count as data transfer error.
There can be multiple `LDF_BEGIN`...`LDF_END` data records (for example for log file purposes).
`LDF_BEGIN` key can have string label value, for example `LDF_BEGIN=Debug data`.

`LDF_COMMENT` key with string value can represent comment. There may be any `LDF_COMMENT` possible.

Errors: `ErrorUnknownLdfSpecialKey(lineNum, name)`, `ErrorNestedLdfDataRecord(lineNum)`, `ErrorLdfDataRecordExcessiveEnd(lineNum)`, `ErrorLdfAppVersionNotInHeader(lineNum)`, `ErrorLdfVersionNotInHeader(lineNum)`.

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
onUserDefinedlNestedTypeBegin(key, typeName)
onUserDefinedlNestedeTypeEnd(key, typeName)
onUserDefinedlType(key, typeName)
```

— these methods can be overriden in `LdfDeserializer` inherited classes.

Errors: `ErrorNonIntegerKeyInArray`, `NegativeKeyInArray`.

## Serializer methods

(Not implemented yet)

```
headerLdfAppVersion(appVersion)
ldfDataRecordBegin(dataRecordLabel)
ldfDataRecordEnd()
ldfComment(comment)
arrayBegin(key)
arrayEnd()
objectBegin(key)
objectEnd()
stringField(key, value)
numberField(key, value)
booleanField(key, value)
userDefinedType(key, type, value)
userDefinedNestedTypeBegin(key, type)
userDefinedNestedTypeEnd(key)
```

Top level may be only object. You're always already inside the object when you start serializing.

Errors: `ErrorUnexpectedArrayClose`, `ErrorUnexpectedObjectClose`, `ErrorUnexpectedUserDefinedNestedTypeClose`, `ErrorArrayNotClosed`, `ErrorObjectNotClosed`, `ErrorUserDefinedNestedTypeNotClosed`

### Serializing single values

Single value can be represented with special `LDF_SINGLE_VALUE` key:

`LDF_SINGLE_VALUE:string=true`

Errors: `ErrorSingleValueExpected` if other fields present.

## User-extensibility: types

User can define custom data types to interpret them manually:

```
h:OrderedHashtable=
-0=
--key:a
--value:1
```

Unknown types are interpreted as `Hashtable`s.