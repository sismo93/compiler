@.strR = private unnamed_addr constant [3 x i8] c"%d\00", align 1
define i32 @readInt() {
  %x = alloca i32, align 4
  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)
  %2 = load i32, i32* %x, align 4
  ret i32 %2
}
declare i32 @__isoc99_scanf(i8*, ...)
@.strP = private unnamed_addr constant [4 x i8] c"%d\0A\00", align 1
define void @println(i32 %x) {
  %1 = alloca i32, align 4
  store i32 %x, i32* %1, align 4
  %2 = load i32, i32* %1, align 4
  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)
  ret void
}
declare i32 @printf(i8*, ...)
define void @defaultFunctionName0(){
%a_0 = alloca i32
store i32 4, i32* %a_0
%b_0 = alloca i32
%1 = load i32, i32* %a_0
store i32 %1, i32* %b_0
%2 = load i32, i32* %b_0
call void @println(i32 %2)
ret void
}

define void @EmptyProgram(){
ret void
}

define i32 @main(){
call void @defaultFunctionName0()
call void @EmptyProgram()
ret i32 0
}
