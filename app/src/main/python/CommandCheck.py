import datefinder

def Date(Command):


    Date= ""
    Dates= datefinder.find_dates(Command)
    for date in Dates:

        Date=date.date()

    return Date
def Time(Command):


    Time= ""
    Dates= datefinder.find_dates(Command)
    for date in Dates:

        Time=date.time()

    return Time
A = ["imran","naqqash"]

def Name(Command):
    N= None

    F= Command.split()
    N= [x for x in A if x in F]
    N = str(N)[1:-1]
    return N
    return Name

def Check(Command):
    Intent = ""
    words = Command.split()

    meet = ["do i have any meeting", "my today's schedule", "my schedule"]


    if (Command == ('hi'))  or (Command==("hello")):

        Intent = "greeting"
    elif((Command == ("how are you")) or (Command == ("how you doing")) or(Command == ("how have you been"))
         or (Command == ("what you doing"))or(Command == ("how do you do"))or(Command == ("how are you"))
         or(Command == ("how you doing"))or(Command == ("what's cooking"))or(Command == ("how are things going"))
         or(Command == ("how's life"))or(Command == ("how are things with you"))or(Command == ("how's life going") )):
        Intent = "AskingAboutYou"
    elif ((Command== ("Who are You") ) or (Command== ("what are you")) or (Command == ("what's your name"))):
        Intent = ("name")
    elif ((Command == ("what time is it")) or (Command == ("what is the time")) or (Command == ("Can you tell me the time "))
          or(Command == ("Tell me time")) ):
        Intent = ("time")
    elif (("open" in words) and ("browser" in words)):
        Intent = ("browser")
    elif(("meeting" in words) and ("schedule" in words)) or (("want" in words) and ("meet" in words)) or (("meeting" in words)) or (("meeting" in words) and ("with" in words)):
        Intent = "meeting"
    elif(("schedule" in words) and ("maintain" in words ) or (("check") in words and ("schdeule")) ):
        Intent = "openschedule"
    elif(ele for ele in meet if(ele in Command) == True):
        Intent = "openschedule"
    else:
        Intent = "nill"

    return Intent